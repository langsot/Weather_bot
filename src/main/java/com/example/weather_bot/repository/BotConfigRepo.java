package com.example.weather_bot.repository;

import com.example.weather_bot.config_bot.BotConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface BotConfigRepo extends MongoRepository<BotConfig, BigInteger> {
}
