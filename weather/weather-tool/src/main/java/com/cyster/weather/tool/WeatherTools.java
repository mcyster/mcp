package com.cyster.weather.tool;

import com.cyster.weather.model.AlertResponse;
import com.cyster.weather.model.ForecastResponse;
import com.cyster.weather.service.WeatherService;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class WeatherTools {

  private final WeatherService weatherService;

  public WeatherTools(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @Tool(description = "Get weather forecast for a specific latitude/longitude")
  public ForecastResponse getWeatherForecastByLocation(
      double latitude, double longitude, ToolContext toolContext) {
    String bearerToken = (String) toolContext.getContext().get("bearerToken");
    if (bearerToken == null) {
      throw new IllegalArgumentException("bearerToken must not be null");
    }
    return weatherService.getWeatherForecastByLocation(latitude, longitude);
  }

  @Tool(
      description =
          "Get weather alerts for a US state. Input is Two-letter US state code(e.g. CA, NY)")
  public AlertResponse getAlerts(String state, ToolContext toolContext) {
    String bearerToken = (String) toolContext.getContext().get("bearerToken");
    if (bearerToken == null) {
      throw new IllegalArgumentException("bearerToken must not be null");
    }
    return weatherService.getAlerts(state);
  }
}
