package com.cyster.weather.service;

public sealed interface WeatherService permits com.cyster.weather.impl.WeatherServiceImpl {
    String getWeatherForecastByLocation(double latitude, double longitude);

    String getAlerts(String state);
}
