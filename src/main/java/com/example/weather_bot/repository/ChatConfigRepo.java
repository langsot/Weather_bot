package com.example.weather_bot.repository;

import com.example.weather_bot.config_bot.ChatConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface ChatConfigRepo extends MongoRepository<ChatConfig, BigInteger> {
    ChatConfig findAllByChatId(Long id);
    void deleteByChatId(Long id);
}
