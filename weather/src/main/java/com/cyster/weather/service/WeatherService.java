package com.cyster.weather.service;

import com.cyster.weather.model.AlertResponse;
import com.cyster.weather.model.ForecastResponse;

/** Service used to retrieve weather information from the NOAA API. */
public interface WeatherService {

  /**
   * Return the weather forecast for the specified location.
   *
   * @param latitude latitude of the location
   * @param longitude longitude of the location
   * @return forecast information
   */
  ForecastResponse getWeatherForecastByLocation(double latitude, double longitude);

  /**
   * Return active alerts for the specified US state.
   *
   * @param state two letter state code
   * @return alert information
   */
  AlertResponse getAlerts(String state);
}
