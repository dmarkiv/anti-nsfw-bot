package com.markiv.telegram;

import com.markiv.handlers.UpdateHandler;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Controller
public class Bot extends TelegramLongPollingBot {
  @Autowired
  private List<UpdateHandler> updateHandlerList;
  @Value("${telegram.bot.username}")
  private String botUsername;
  @Value("${telegram.bot.token}")
  private String botToken;

  @Override
  public void onUpdateReceived(Update update) {
    dispatchUpdate(update);
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  private void dispatchUpdate(Update update) {
    updateHandlerList.stream()
      .filter(updateHandler -> updateHandler.canHandle(update))
      .findAny()
      .ifPresent(updateHandler -> updateHandler.handle(update));
  }
}