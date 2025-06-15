package com.cyster.weather.model;

import java.util.List;

/**
 * Response returned from {@link com.cyster.weather.service.WeatherService} when requesting a
 * forecast.
 */
public record ForecastResponse(List<ForecastPeriod> periods) {

  /** Forecast details for a single time period. */
  public record ForecastPeriod(
      String name,
      Integer temperature,
      String temperatureUnit,
      String windSpeed,
      String windDirection,
      String detailedForecast) {}
}
