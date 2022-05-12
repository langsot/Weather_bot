package com.example.weather_bot.facade;

import com.example.weather_bot.service.ChatConfigService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KeyboardService {

    private ChatConfigService chatConfigService;

    private final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

    public KeyboardService(ChatConfigService chatConfigService) {
        this.chatConfigService = chatConfigService;
    }

    public InlineKeyboardMarkup setChooseCityMarkup(Long id) {
        List<InlineKeyboardButton> keyboardBut = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText(chatConfigService.getCity(id));
        button.setCallbackData(getCurrentCityNowButton(chatConfigService.getCity(id)));

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Другой");
        button1.setCallbackData(getChooseCityNowButtonData());

        keyboardBut.add(button);
        keyboardBut.add(button1);
        keyboardMarkup.setKeyboard(Arrays.asList(keyboardBut));

        return keyboardMarkup;
    }

    public String getChooseCityNowButtonData() {
        return "Выбирите необходимый город";
    }

    public String getCurrentCityNowButton(String city) {
        return "Сейчас " + city;
    }
}
