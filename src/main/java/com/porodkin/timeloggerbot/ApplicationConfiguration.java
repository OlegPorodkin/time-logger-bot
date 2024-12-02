package com.porodkin.timeloggerbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public RestTemplate timeloggerRestTemplate(@Value("${api.time-logger}") String timeLoggerApi) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(timeLoggerApi));
        return restTemplate;
    }

    @Bean
    public TimeLoggerTelegramBot timeloggerBot(
            @Value("${telegram.bot.username}") String tgUserName,
            @Value("${telegram.bot.token}") String tgToken,
            TimeLoggerApiRestClient timeloggerRestTemplate
    ) {
        TimeLoggerTelegramBot timeLoggerTelegramBot = new TimeLoggerTelegramBot(tgToken, tgUserName, timeloggerRestTemplate);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(timeLoggerTelegramBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return timeLoggerTelegramBot;
    }
}
