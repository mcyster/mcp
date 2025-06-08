package com.cyster.weather.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.cyster.weather.service.WeatherService;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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

    public record Points(Properties properties) {
        public record Properties(String forecast) {
        }
    }

    public record Forecast(Properties properties) {
        public record Properties(List<Period> periods) {
        }

        public record Period(Integer number,
                String name,
                String startTime,
                String endTime,
                Boolean isDayTime,
                Integer temperature,
                String temperatureUnit,
                String temperatureTrend,
                ValueUnit probabilityOfPrecipitation,
                String windSpeed,
                String windDirection,
                String icon,
                String shortForecast,
                String detailedForecast,
                String cwa,
                String gridId,
                Integer gridX,
                Integer gridY) {
        }

        public record ValueUnit(Integer value, String unitCode) {
        }
    }

    public record Alert(@JsonProperty("features") List<Feature> features) {
        public record Feature(@JsonProperty("properties") Properties properties) {
        }

        public record Properties(@JsonProperty("event") String event,
                @JsonProperty("areaDesc") String areaDesc,
                @JsonProperty("severity") String severity,
                @JsonProperty("description") String description,
                @JsonProperty("instruction") String instruction) {
        }
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

        String forecastText = forecast.properties().periods().stream().map(period ->
            String.format("""
                    %s:
                    Temperature: %s %s
                    Wind: %s %s
                    Forecast: %s
                    """,
                    period.name(),
                    period.temperature(),
                    period.temperatureUnit(),
                    period.windSpeed(),
                    period.windDirection(),
                    period.detailedForecast())
        ).collect(Collectors.joining());

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
                    """,
                    feature.properties().event(),
                    feature.properties().areaDesc(),
                    feature.properties().severity(),
                    feature.properties().description(),
                    feature.properties().instruction()))
            .collect(Collectors.joining("\n"));
    }
}
