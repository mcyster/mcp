package com.cyster.weather.impl;

import com.cyster.weather.service.*;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {
    private static final String BASE_URL = "https://api.weather.gov";

    private final RestClient restClient;

    public WeatherServiceImpl() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
                .build();
    }

    @Override
    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(double latitude, double longitude) {
        var points = restClient.get()
                .uri("/points/{latitude},{longitude}", latitude, longitude)
                .retrieve()
                .body(Points.class);

        if (points == null || points.properties() == null) {
            return "Could not retrieve forecast information for the given location.";
        }

        var forecast = restClient.get().uri(points.properties().forecast()).retrieve().body(Forecast.class);

        if (forecast == null || forecast.properties() == null) {
            return "Could not retrieve forecast information for the given location.";
        }

        String forecastText = forecast.properties().periods()
                .stream()
                .map(period -> String.format("""
                        %s:
                        Temperature: %s %s
                        Wind: %s %s
                        Forecast: %s
                        """, period.name(), period.temperature(), period.temperatureUnit(),
                        period.windSpeed(), period.windDirection(), period.detailedForecast()))
                .collect(Collectors.joining());

        return forecastText;
    }

    @Override
    @Tool(description = "Get weather alerts for a US state. Input is Two-letter US state code (e.g. CA, NY)")
    public String getAlerts(String state) {
        Alert alert = restClient.get().uri("/alerts/active/area/{state}", state).retrieve().body(Alert.class);

        if (alert == null || alert.features() == null) {
            return "Could not retrieve alert information for the given state.";
        }

        return alert.features()
                .stream()
                .map(feature -> String.format("""
                    Event: %s
                    Area: %s
                    Severity: %s
                    Description: %s
                    Instructions: %s
                    """, feature.properties().event(), feature.properties().areaDesc(), feature.properties().severity(),
                        feature.properties().description(), feature.properties().instruction()))
                .collect(Collectors.joining("\n"));
    }
}
