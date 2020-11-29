package com.markiv.handlers;

import com.markiv.service.ImageClassificationService;
import com.markiv.telegram.Bot;
import com.markiv.telegram.TelegramFileDownloader;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Controller
@Lazy
@Slf4j
public class ImageUpdateHandler implements UpdateHandler {
  Bot bot;
  ImageClassificationService imageClassificationService;
  TelegramFileDownloader fileDownloader;

  @Autowired
  public ImageUpdateHandler(Bot bot, ImageClassificationService imageClassificationService,
                            TelegramFileDownloader fileDownloader) {
    this.bot = bot;
    this.imageClassificationService = imageClassificationService;
    this.fileDownloader = fileDownloader;
  }


  @Override
  public void handle(Update update) {
    Message message = update.getMessage();
    File imageFile = downloadHighResPhoto(message);

    if ((imageClassificationService.isSafeForWork(imageFile))) {
      log.info("Message " + message.getMessageId() + " in chat " + message.getChatId() + " is ok");
    } else {
      int messageId = message.getMessageId();
      long chatId = message.getChatId();


      DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
      log.info("Message " + message.getMessageId() + " in chat " + message.getChatId() + " isn't safe");


      try {
        bot.execute(deleteMessage);
        log.info("Message " + message.getMessageId() + " in chat " + message.getChatId() + " was deleted");
      } catch (TelegramApiException e) {
        log.info("Message was not deleted " + e);
      }
    }
  }


  @Override
  public boolean canHandle(Update update) {
    if (!update.hasMessage()) {
      return false;
    }
    return update.getMessage().hasPhoto();
  }

  private File downloadHighResPhoto(Message message) {
    List<PhotoSize> photos = message.getPhoto();
    //The first one is always the biggest
    PhotoSize highResPhoto = photos.get(photos.size() - 1);
    return fileDownloader.downloadFile(highResPhoto.getFileId());
  }
}
