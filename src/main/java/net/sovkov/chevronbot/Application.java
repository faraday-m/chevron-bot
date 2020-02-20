package net.sovkov.chevronbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Optional;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {
  public static void main(String[] args) {
    //Add this line to initialize bots context
    ApiContextInitializer.init();
    SpringApplication app = new SpringApplication(Application.class);
    app.setDefaultProperties(Collections
        .singletonMap("server.port", Optional.ofNullable(System.getenv("PORT")).orElse("5000")));
    app.run(Application.class, args);
  
  
    try (ServerSocket serverSocket = new ServerSocket(Integer.valueOf(System.getenv("PORT")))) {
      while (true) {
        Socket clientSocket = serverSocket.accept();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}