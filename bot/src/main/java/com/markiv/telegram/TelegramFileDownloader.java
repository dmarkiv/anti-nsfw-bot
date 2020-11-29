package com.markiv.telegram;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramFileDownloader {
  private Bot bot;

  @Autowired
  public TelegramFileDownloader(Bot bot) {
    this.bot = bot;
  }

  public File downloadFile(String id) {
    GetFile getFileMethod = new GetFile();
    getFileMethod.setFileId(id);
    try {
      var telegramFile = bot.execute(getFileMethod);
      return bot.downloadFile(telegramFile);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
    return null;
  }
}
