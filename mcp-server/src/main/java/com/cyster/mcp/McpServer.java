package com.cyster.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cyster.weather.service.WeatherService;

@SpringBootApplication(scanBasePackages = {"com.cyster.mcp", "com.cyster.weather"})
public class McpServer {
    public static void main(String[] args) {
        SpringApplication.run(McpServer.class, args);
    }

    @Bean
    public ToolCallbackProvider environmentTools(WeatherService weatherService, ToolContextService environmentService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService, environmentService).build();
    }
        
}
