package com.markiv.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
  void handle(Update update);

  boolean canHandle(Update update);
}
