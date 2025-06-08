package com.cyster.weather.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Response returned from {@link com.cyster.weather.service.WeatherService} when requesting a
 * forecast.
 */
@Schema(description = "Weather forecast response")
public record ForecastResponse(
    @Schema(description = "Forecast periods") List<ForecastPeriod> periods) {

  /** Forecast details for a single time period. */
  @Schema(description = "Forecast information for a period")
  public record ForecastPeriod(
      @Schema(description = "Name of the period", example = "Tonight") String name,
      @Schema(description = "Temperature value") Integer temperature,
      @Schema(description = "Temperature units", example = "F") String temperatureUnit,
      @Schema(description = "Wind speed", example = "5 mph") String windSpeed,
      @Schema(description = "Wind direction", example = "NW") String windDirection,
      @Schema(description = "Detailed forecast text") String detailedForecast) {}
}
