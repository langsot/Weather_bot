package com.example.weather_bot.controller;

import com.example.weather_bot.entity.WeatherNow;
import com.example.weather_bot.service.BotConfigService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class WeatherRestTemplate {

    private final RestTemplate restTemplate;

    private final BotConfigService botConfig;

    public WeatherRestTemplate(RestTemplate restTemplate, BotConfigService botConfig) {
        this.restTemplate = restTemplate;
        this.botConfig = botConfig;
    }

    public WeatherNow getWeatherNow(String city) {
        try {
            return restTemplate.getForObject(botConfig.getWeatherApiTemp().replace("{city}", city), WeatherNow.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isCity(String city) throws IOException {
        URL weatherApiUrl = new URL(botConfig.getWeatherApiTemp().replace("{city}", city));

        HttpURLConnection weatherApiConnection = (HttpURLConnection) weatherApiUrl.openConnection();
        weatherApiConnection.setRequestMethod("GET");
        weatherApiConnection.connect();
        return weatherApiConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

}
