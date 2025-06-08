package com.cyster.weather.service;

public interface WeatherService {
  String getWeatherForecastByLocation(double latitude, double longitude);

  String getAlerts(String state);
}
