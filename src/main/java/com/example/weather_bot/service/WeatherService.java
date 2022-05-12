package com.example.weather_bot.service;

import com.example.weather_bot.controller.WeatherRestTemplate;
import com.example.weather_bot.entity.WeatherNow;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {
    private WeatherRestTemplate restTemplate;

    public WeatherService(WeatherRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isCity(String city) throws IOException {
        return restTemplate.isCity(city);
    }

    public WeatherNow getCurrentWeather(String city) {
        return restTemplate.getWeatherNow(city);
    }
}
