package com.example.weather_bot.service;

import com.example.weather_bot.config_bot.Command;
import com.example.weather_bot.repository.BotConfigRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotConfigService {

    private final BotConfigRepo botConfigRepo;

    public BotConfigService(BotConfigRepo botConfigRepo) {
        this.botConfigRepo = botConfigRepo;
    }

    public String getName() {
        return botConfigRepo.findAll().get(0).getName();
    }

    public String getAccessToken() {
        return botConfigRepo.findAll().get(0).getAccessToken();
    }

    public String getWeatherApiTemp() {
        return botConfigRepo.findAll().get(0).getWeatherApiTemp();
    }

    public String getTelegramCallbackAnswerTemp() {
        return botConfigRepo.findAll().get(0).getTelegramCallbackAnswerTemp();
    }

    public List<Command> getAllCommands() {
        return botConfigRepo.findAll().get(0).getCommands();
    }


}
