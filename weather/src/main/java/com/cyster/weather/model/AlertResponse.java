package com.cyster.weather.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Response returned from {@link com.cyster.weather.service.WeatherService} when requesting alerts.
 */
@Schema(description = "Weather alerts response")
public record AlertResponse(@Schema(description = "Active alerts") List<Alert> alerts) {

  /** Information about a single alert. */
  @Schema(description = "Alert details")
  public record Alert(
      @Schema(description = "Event name", example = "Flood Warning") String event,
      @Schema(description = "Area description") String area,
      @Schema(description = "Severity level", example = "Severe") String severity,
      @Schema(description = "Alert description") String description,
      @Schema(description = "Safety instructions") String instruction) {}
}
