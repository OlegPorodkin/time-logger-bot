package com.porodkin.timeloggerbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;


public class TimeLoggerTelegramBot extends TelegramLongPollingBot {

    public static final Logger log = LoggerFactory.getLogger(TimeLoggerTelegramBot.class);

    private final String botUsername;
    private final TimeLoggerApiRestClient timeLoggerApiRestClient;

    public TimeLoggerTelegramBot(
            String botToken,
            String botUsername,
            TimeLoggerApiRestClient timeLoggerApiRestClient
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.timeLoggerApiRestClient = timeLoggerApiRestClient;
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            long userId = message.getFrom().getId();
            log.info("Received time logger message from : " + userId);

            if (message.getText().equalsIgnoreCase("Start")) {
                log.info(chatId + " start");
                timeLoggerApiRestClient.sendStartRequest(userId);
                sendResponse(chatId, "Work session started!");
            } else if (message.getText().equalsIgnoreCase("End")) {
                log.info(chatId + " end");
                timeLoggerApiRestClient.sendEndRequest(userId);
                sendResponse(chatId, "Work session ended!");
            } else {
                sendResponse(chatId, "Please use the buttons to interact.");
            }
        }
    }

    private void sendResponse(String chatId, String text) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text);
            message.setReplyMarkup(createKeyboard());
            execute(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Start"));
        row.add(new KeyboardButton("End"));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
