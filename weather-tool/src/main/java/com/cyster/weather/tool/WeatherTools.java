package com.cyster.weather.tool;

import com.cyster.weather.service.WeatherService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class WeatherTools {

    private final WeatherService weatherService;

    public WeatherTools(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(double latitude, double longitude) {
        return weatherService.getWeatherForecastByLocation(latitude, longitude);
    }

    @Tool(description = "Get weather alerts for a US state. Input is Two-letter US state code(e.g. CA, NY)")
    public String getAlerts(String state) {
        return weatherService.getAlerts(state);
    }
}
