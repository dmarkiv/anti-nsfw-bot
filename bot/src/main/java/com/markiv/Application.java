package com.markiv;

import com.markiv.telegram.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Configuration
@SpringBootConfiguration
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    ApiContextInitializer.init();
    ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

    Bot bot = context.getBean(Bot.class);

    TelegramBotsApi botsApi = new TelegramBotsApi();
    try {
      botsApi.registerBot(bot);
    } catch (TelegramApiRequestException e) {
      e.printStackTrace();
    }
  }
}
