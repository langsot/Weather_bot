package com.example.weather_bot.facade;

import com.example.weather_bot.bot.WeatherBot;
import com.example.weather_bot.enums.BotState;
import com.example.weather_bot.enums.KeyboardType;
import com.example.weather_bot.enums.MainCommand;
import com.example.weather_bot.service.ChatConfigService;
import com.example.weather_bot.service.WeatherService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Locale;

@Component
public class WeatherBotFacade {

    private final ChatConfigService chatConfigService;
    private final MassageGeneration massageGeneration;
    private final WeatherService weatherService;
    private final KeyboardService keyboardService;
    private final WeatherBot weatherBot;
    private final CallbackAnswer callbackAnswer;

    public WeatherBotFacade(ChatConfigService chatConfigService, MassageGeneration massageGeneration, WeatherService weatherService, KeyboardService keyboardService, WeatherBot weatherBot, CallbackAnswer callbackAnswer) {
        this.chatConfigService = chatConfigService;
        this.massageGeneration = massageGeneration;
        this.weatherService = weatherService;
        this.keyboardService = keyboardService;
        this.weatherBot = weatherBot ;
        this.callbackAnswer = callbackAnswer;
    }

    public void handleUpdate(Update update) throws IOException, InterruptedException {
        Long id;
        String massage;
        String userFirstName = "";

        if (update.hasMessage()) {
            id = update.getMessage().getChatId();
            massage = update.getMessage().getText().toUpperCase(Locale.ROOT).replace("/", "");
            userFirstName = update.getMessage().getChat().getFirstName();
        } else if (update.hasChannelPost()) {
            id = update.getChannelPost().getChatId();
            massage = update.getChannelPost().getText().toUpperCase(Locale.ROOT).replace("/","");
            userFirstName = update.getChannelPost().getChat().getFirstName();
        }else if (update.hasCallbackQuery()){
            callbackAnswer.callbackAnswer(update.getCallbackQuery().getId());

            id = update.getCallbackQuery().getMessage().getChatId();
            massage = update.getCallbackQuery().getData().toUpperCase(Locale.ROOT);
            sendMessage(update,update.getCallbackQuery().getData());

            if (massage.equals(keyboardService.getChooseCityNowButtonData().toUpperCase(Locale.ROOT))){
                chatConfigService.setBotState(id, BotState.SEARCH_NOW);
                return;
            }

            else if (massage.equals(keyboardService.getCurrentCityNowButton(chatConfigService.getCity(id)).toUpperCase(Locale.ROOT))){
                chatConfigService.setBotState(id,BotState.NOW);
            }
        }

        else if (update.hasMyChatMember()) {
            if (update.getMyChatMember().getNewChatMember().getStatus().equals("kicked")){
                chatConfigService.deleteChat(update.getMyChatMember().getChat().getId());
            }

            return;
        }else {

            return;
        }

        if (!chatConfigService.isChatInit(id)) {
            chatConfigService.initChat(id);
            sendMessage(update, massageGeneration.generateStartMassage(userFirstName));
        }else{
            handleBotState(update, id, massage, userFirstName);
        }
    }


    private Long setChatIdToMessageBuilder(Update update, SendMessage.SendMessageBuilder messageBuilder){
        Long chatId = null;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            messageBuilder.chatId(update.getMessage().getChatId().toString());
        } else if (update.hasChannelPost()) {
            chatId = update.getChannelPost().getChatId();
            messageBuilder.chatId(update.getChannelPost().getChatId().toString());
        }else if (update.hasCallbackQuery()){
            chatId = update.getCallbackQuery().getMessage().getChatId();
            messageBuilder.chatId(update.getCallbackQuery().getMessage().getChatId().toString());
        }
        return chatId;
    }

    private void sendMessage(Update update,String messageText){
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();

        Long chatId = setChatIdToMessageBuilder(update,messageBuilder);

        messageBuilder.text(messageText);

        try {
            weatherBot.execute(messageBuilder.build());
        }catch (TelegramApiException telegramApiException){
            telegramApiException.printStackTrace();
        }
    }

    private void sendMessage(Update update, String messageText, KeyboardType keyboardType) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();

        Long chatId = setChatIdToMessageBuilder(update, messageBuilder);

        messageBuilder.text(messageText);

        if (keyboardType == KeyboardType.CITY_CHOOSE) {
            messageBuilder.replyMarkup(keyboardService.setChooseCityMarkup(chatId));
        }

        try {
            weatherBot.execute(messageBuilder.build());
        }catch (TelegramApiException telegramApiException){
            telegramApiException.printStackTrace();
        }
    }

    private void handleBotState(Update update,Long chatId,String messageText,String userFirstName) throws IOException {
        BotState botState = chatConfigService.getBotState(chatId);

        if (messageText.equals(MainCommand.START.name())) {
            chatConfigService.setBotState(chatId,BotState.DEFAULT);
            sendMessage(update,massageGeneration.generateStartMassage(userFirstName));
            return;
        }

        if (messageText.equals(MainCommand.CANCEL.name())){
            if (botState == BotState.DEFAULT){
                sendMessage(update,"Нет активной команды для отклонения");
            }else {
                chatConfigService.setBotState(chatId,BotState.DEFAULT);
                sendMessage(update,massageGeneration.generateSuccessCancel());
                return;
            }
        }

        switch (botState) {
            case DEFAULT: {

                if (messageText.equals(MainCommand.HELP.name())) {
                    sendMessage(update, massageGeneration.generateHelpMassage());
                }

                else if (messageText.equals(MainCommand.SETCITY.name())) {
                    chatConfigService.setBotState(chatId, BotState.SET_CITY);
                    sendMessage(update, "Введите новый стандартный город");
                }

                else if (messageText.equals(MainCommand.CITY.name())) {
                    if (chatConfigService.getCity(chatId) != null && !chatConfigService.getCity(chatId).equals("")) sendMessage(update, massageGeneration.generateSuccessGetCity(chatConfigService.getCity(chatId)));
                    else sendMessage(update, massageGeneration.generateErrorGetCity());
                }

                else if (messageText.equals(MainCommand.NOW.name())) {
                    chatConfigService.setBotState(chatId, BotState.NOW);
                    sendMessage(update, "Выберите город", KeyboardType.CITY_CHOOSE);
                }

                break;
            }

            case SET_CITY: {

                if (weatherService.isCity(messageText.toLowerCase(Locale.ROOT))) {
                    chatConfigService.setCity(chatId, messageText.charAt(0)+messageText.substring(1).toLowerCase(Locale.ROOT));
                    chatConfigService.setBotState(chatId, BotState.DEFAULT);
                    sendMessage(update, massageGeneration.generateSuccessSetCity(chatConfigService.getCity(chatId)));
                }

                else sendMessage(update, massageGeneration.generateErrorCity());

                break;
            }

            case NOW: {

                if (messageText.equals(keyboardService.getChooseCityNowButtonData().toUpperCase(Locale.ROOT)))
                {
                    chatConfigService.setBotState(chatId,BotState.SEARCH_NOW);
                }

                else {
                    chatConfigService.setBotState(chatId,BotState.DEFAULT);
                    sendMessage(update,massageGeneration.generateCurrentWeather(chatConfigService.getCity(chatId)));
                }
                break;
            }

            case SEARCH_NOW: {
                if (!weatherService.isCity(messageText)){
                    sendMessage(update,massageGeneration.generateErrorCity());
                }

                else {
                    sendMessage(update,massageGeneration.generateCurrentWeather(messageText.charAt(0) + messageText.substring(1).toLowerCase(Locale.ROOT)));
                    chatConfigService.setBotState(chatId,BotState.DEFAULT);
                }

                break;
            }
        }
    }
}

