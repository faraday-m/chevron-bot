package net.sovkov.chevronbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChevronBot extends TelegramLongPollingBot {
  private static final Logger logger = LoggerFactory.getLogger(ChevronBot.class);
  
  private static final String TOKEN = System.getProperty("bot.token");
  private static final String USERNAME = "lprchevronbot";
  
  
  public ChevronBot() {}
  
  public String getBotToken() {
    return TOKEN;
  }
  
  public String getBotUsername() {
    return USERNAME;
  }
  
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      processCommand(update);
    } else if (update.hasCallbackQuery()) {
     // processCallbackQuery(update);
    }
  }
  
  private void processCommand(Update update) {
    SendMessage answer = new SendMessage();
    String msg = update.getMessage().getText();
    Long chatId = update.getMessage().getChatId();
    if ("/start".equalsIgnoreCase(msg)) {
      answer.setText("Привет!\n\n" +
          "Этот бот генерирует шаблон модной нашивки, по которому её можно где-нибудь заказать, а затем пришить на видное место.\n" +
          "Введите фразу из 1-10 слов (не более 25 символов каждое), и бот сгенерирует подходящий шеврон");
      answer.setChatId(chatId);
      try {
        execute(answer);
        logger.info("Sent message \"{}\" to {}", msg, chatId);
      } catch (TelegramApiException e) {
        logger.error("Failed to send message \"{}\" to {} due to error: {}", msg, chatId, e.getMessage());
      }
    } else if (isMsgValid(msg)) {
      try {
        List<String> words = Arrays.asList(msg.trim().split("\\s+")).stream()
            .map(String::toUpperCase).collect(Collectors.toList());
        String data = ImageGenerator.setLinespaces(words);
        BufferedImage img = ImageGenerator.textToImage(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        baos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        SendDocument doc = new SendDocument().setDocument("chevron.png", bais);
        execute(doc);
        logger.info("Sent message \"{}\" to {}", msg, chatId);
      } catch (Exception e) {
          logger.error("Failed to send message \"{}\" to {} due to error: {}", msg, chatId, e.getMessage());
      }
    } else {
      answer.setText("Что-то пошло не так. Перезапустите бота командой /start!");
      answer.setChatId(chatId);
      try {
        execute(answer);
        logger.info("Sent message \"{}\" to {}", msg, chatId);
      } catch (TelegramApiException e) {
        logger.error("Failed to send message \"{}\" to {} due to error: {}", msg, chatId, e.getMessage());
      }
    }
    
  }
  
  private boolean isMsgValid(String msg) {
    List<String> words = Arrays.asList(msg.trim().split("\\s+"));
    if (words.size() > 10) {
      return false;
    }
    if (words.stream().anyMatch(it -> it.length() > 25)) {
      return false;
    }
    return true;
  }
  
  @PostConstruct
  public void start() {
    logger.info("username: {}, token: {}", USERNAME, TOKEN);
  }
}
