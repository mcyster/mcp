package com.cyster.weather.model;

import java.util.List;

/**
 * Response returned from {@link com.cyster.weather.service.WeatherService} when requesting alerts.
 */
public record AlertResponse(List<Alert> alerts) {

  /** Information about a single alert. */
  public record Alert(
      String event, String area, String severity, String description, String instruction) {}
}
