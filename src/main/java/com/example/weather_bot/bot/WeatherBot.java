package com.example.weather_bot.bot;

import com.example.weather_bot.facade.WeatherBotFacade;
import com.example.weather_bot.service.BotConfigService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class WeatherBot extends TelegramLongPollingBot {

    private final BotConfigService botConfigService;
    private final WeatherBotFacade weatherBotFacade;

    public WeatherBot(BotConfigService botConfigService, WeatherBotFacade weatherBotFacade) {
        this.botConfigService = botConfigService;
        this.weatherBotFacade = weatherBotFacade;
    }

    @Override
    public String getBotUsername() {
        return botConfigService.getName();
    }

    @Override
    public String getBotToken() {
        return botConfigService.getAccessToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        weatherBotFacade.handleUpdate(update);
    }
}
