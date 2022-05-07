package com.example.weather_bot.service;

import com.example.weather_bot.config_bot.ChatConfig;
import com.example.weather_bot.enums.BotState;
import com.example.weather_bot.repository.ChatConfigRepo;
import org.springframework.stereotype.Service;

@Service
public class ChatConfigService {

    private final ChatConfigRepo chatConfigRepo;

    public ChatConfigService(ChatConfigRepo chatConfigRepo) {
        this.chatConfigRepo = chatConfigRepo;
    }

    public boolean isChatInit(Long id) {
        return chatConfigRepo.findAllByChatId(id) != null;
    }

    public void initChat(Long id) {
        chatConfigRepo.save(new ChatConfig(id, BotState.DEFAULT));
    }

    public void deleteChat(Long id) {
        chatConfigRepo.deleteByChatId(id);
    }

    public void setBotState(Long id, BotState botState) {
        ChatConfig chatConfig = chatConfigRepo.findAllByChatId(id);
        chatConfig.setBotState(botState);
        chatConfigRepo.save(chatConfig);
    }

    public BotState getBotState(Long id) {
        return chatConfigRepo.findAllByChatId(id).getBotState();
    }

    public void setCity(Long id, String city) {
        ChatConfig chatConfig = chatConfigRepo.findAllByChatId(id);
        chatConfig.setCity(city);
        chatConfigRepo.save(chatConfig);
    }

    public String getCity(Long id) {
        return chatConfigRepo.findAllByChatId(id).getCity();
    }
}
