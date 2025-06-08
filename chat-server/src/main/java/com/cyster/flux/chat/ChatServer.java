package com.cyster.flux.chat;

import com.cyster.rest.RestPackageMarker;
import com.cyster.weather.WeatherServicePackageMarker;
import com.cyster.weather.tool.WeatherToolPackageMarker;
import com.cyster.weather.tool.WeatherTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
    scanBasePackageClasses = {
      ChatServer.class,
      WeatherServicePackageMarker.class,
      WeatherToolPackageMarker.class,
      RestPackageMarker.class
    })
public class ChatServer {
  public static void main(String[] args) {
    SpringApplication.run(ChatServer.class, args);
  }

  @Bean
  public ToolCallbackProvider toolCallbackProvider(WeatherTools weatherTools) {
    return MethodToolCallbackProvider.builder().toolObjects(weatherTools).build();
  }
}
