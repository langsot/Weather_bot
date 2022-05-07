package com.example.weather_bot.facade;

import com.example.weather_bot.entity.WeatherNow;
import com.example.weather_bot.service.BotConfigService;
import com.example.weather_bot.service.WeatherService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Service;

@Service
public class MassageGeneration {

    private final BotConfigService botConfigService;
    private final WeatherService weatherService;
    private String massage;

    public MassageGeneration(BotConfigService botConfigService, WeatherService weatherService) {
        this.botConfigService = botConfigService;
        this.weatherService = weatherService;
    }

    public String generateStartMassage(String name) {
        return EmojiParser.parseToUnicode("Привет, " + name + " :wave: \nЧтобы узнать, как мной пользоваться - введите /help");
    }

    public String generateHelpMassage() {
        massage = ":sunny: Вот мои доступные комманды :sunny:\n\n";
        botConfigService.getAllCommands().forEach(command -> {
            massage += command.getName() + " - " + command.getDescription() + "\n";
        });
        return EmojiParser.parseToUnicode(massage);
    }

    public String generateSuccessCancel() {
        return EmojiParser.parseToUnicode(":white_check_mark: Активная команда успешно отклонена");
    }

    public String generateSuccessSetCity(String city) {
        return EmojiParser.parseToUnicode(":white_check_mark: Новый стандартный город - " + city);
    }

    public String generateErrorCity() {
        return EmojiParser.parseToUnicode(":x: Такого города не существует");
    }

    public String generateSuccessGetCity(String city) {
        return EmojiParser.parseToUnicode(":cityscape: Стандартный город - " + city);
    }

    public String generateErrorGetCity() {
        return EmojiParser.parseToUnicode(":x: Стандартный город не назначен");
    }

    public String generateCurrentWeather(String city) {
        WeatherNow weatherNow = weatherService.getCurrentWeather(city);
        return EmojiParser.parseToUnicode("Текущая погода\n\n" +
                "В городе " + city + " " + weatherNow.getWeather().get(0).getDescription() + "\n" +
                ":thermometer: Температура: " + weatherNow.getMain().getTemp() + "°C, ощущается как " +
                weatherNow.getMain().getFellsLike() + "°C");
    }
}
