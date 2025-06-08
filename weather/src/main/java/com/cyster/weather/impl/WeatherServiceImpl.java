package com.cyster.weather.impl;

import com.cyster.weather.model.AlertResponse;
import com.cyster.weather.model.ForecastResponse;
import com.cyster.weather.service.WeatherService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherServiceImpl implements WeatherService {
  private static final String BASE_URL = "https://api.weather.gov";

  private final RestClient restClient;

  public WeatherServiceImpl() {
    this.restClient =
        RestClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader("Accept", "application/geo+json")
            .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
            .build();
  }

  public record Points(Properties properties) {
    public record Properties(String forecast) {}
  }

  public record Forecast(Properties properties) {
    public record Properties(List<Period> periods) {}

    public record Period(
        Integer number,
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
        Integer gridY) {}

    public record ValueUnit(Integer value, String unitCode) {}
  }

  public record Alert(@JsonProperty("features") List<Feature> features) {
    public record Feature(@JsonProperty("properties") Properties properties) {}

    public record Properties(
        @JsonProperty("event") String event,
        @JsonProperty("areaDesc") String areaDesc,
        @JsonProperty("severity") String severity,
        @JsonProperty("description") String description,
        @JsonProperty("instruction") String instruction) {}
  }

  @Override
  public ForecastResponse getWeatherForecastByLocation(double latitude, double longitude) {
    var points =
        restClient
            .get()
            .uri("/points/{latitude},{longitude}", latitude, longitude)
            .retrieve()
            .body(Points.class);

    if (points == null || points.properties() == null) {
      return new ForecastResponse(java.util.List.of());
    }

    var forecast =
        restClient.get().uri(points.properties().forecast()).retrieve().body(Forecast.class);

    if (forecast == null || forecast.properties() == null) {
      return new ForecastResponse(java.util.List.of());
    }

    List<ForecastResponse.ForecastPeriod> periods =
        forecast.properties().periods().stream()
            .map(
                p ->
                    new ForecastResponse.ForecastPeriod(
                        p.name(),
                        p.temperature(),
                        p.temperatureUnit(),
                        p.windSpeed(),
                        p.windDirection(),
                        p.detailedForecast()))
            .toList();

    return new ForecastResponse(periods);
  }

  @Override
  public AlertResponse getAlerts(String state) {
    Alert alert =
        restClient.get().uri("/alerts/active/area/{state}", state).retrieve().body(Alert.class);

    if (alert == null || alert.features() == null) {
      return new AlertResponse(java.util.List.of());
    }

    List<AlertResponse.Alert> alerts =
        alert.features().stream()
            .map(
                feature ->
                    new AlertResponse.Alert(
                        feature.properties().event(),
                        feature.properties().areaDesc(),
                        feature.properties().severity(),
                        feature.properties().description(),
                        feature.properties().instruction()))
            .toList();

    return new AlertResponse(alerts);
  }
}
