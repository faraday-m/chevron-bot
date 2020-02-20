package net.sovkov.chevronbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {
  
  public static void main(String[] args) {
    //Add this line to initialize bots context
    ApiContextInitializer.init();
    
    SpringApplication.run(Application.class, args);
  }
}