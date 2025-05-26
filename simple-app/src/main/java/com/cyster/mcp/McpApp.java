package com.cyster.mcp;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpApp {

    public static void main(String[] args) {
        SpringApplication.run(McpApp.class, args);
    }

    @Bean
    public ToolCallbackProvider environentTools(WeatherService weatherService, EnvironmentService environmentService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService, environmentService).build();
    }

}