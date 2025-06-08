package com.cyster.flux.chat;

import com.cyster.weather.WeatherServicePackageMarker;
import com.cyster.weather.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
    scanBasePackageClasses = {ChatServer.class, WeatherServicePackageMarker.class})
public class ChatServer {
  public static void main(String[] args) {
    SpringApplication.run(ChatServer.class, args);
  }

  @Bean
  public ToolCallbackProvider toolCallbackProvider(WeatherService weatherService) {
    return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
  }
}
