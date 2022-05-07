package com.example.weather_bot.config_bot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class BotConfig {

    @Id
    private BigInteger id;

    private String name;

    private String accessToken;

    private String weatherApiTemp;

    private String telegramCallbackAnswerTemp;

    private List<Command> commands;
}
