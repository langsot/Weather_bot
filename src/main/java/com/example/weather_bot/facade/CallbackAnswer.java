package com.example.weather_bot.facade;

import com.example.weather_bot.service.BotConfigService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CallbackAnswer {

    private BotConfigService botConfigService;

    public CallbackAnswer(BotConfigService botConfigService) {
        this.botConfigService = botConfigService;
    }

    public void callbackAnswer(String callbackId) throws IOException, InterruptedException {
        HttpClient telegramApiClient = HttpClient.newHttpClient();
        HttpRequest telegramCallbackAnswerReq = HttpRequest.newBuilder(URI
                        .create(botConfigService
                                .getTelegramCallbackAnswerTemp()
                                .replace("{token}",botConfigService.getAccessToken())
                                .replace("{id}",callbackId)))
                .GET().build();

        telegramApiClient.send(telegramCallbackAnswerReq, HttpResponse.BodyHandlers
                .ofString());
    }
}
