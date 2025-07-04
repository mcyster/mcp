package com.cyster.mcp;

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
      McpServer.class,
      WeatherServicePackageMarker.class,
      WeatherToolPackageMarker.class
    })
public class McpServer {
  public static void main(String[] args) {
    SpringApplication.run(McpServer.class, args);
  }

  @Bean
  public ToolCallbackProvider environmentTools(
      WeatherTools weatherTools, ToolContextService environmentService) {
    return MethodToolCallbackProvider.builder()
        .toolObjects(weatherTools, environmentService)
        .build();
  }
}
