package com.cyster.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class McpApp {
    public static void main(String[] args) {
        SpringApplication.run(McpApp.class, args);
    }

    @Bean
    public ToolCallbackProvider environentTools(WeatherService weatherService, ToolContextService environmentService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService, environmentService).build();
    }
        
}