package com.cyster.weather.service;

import java.util.List;

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
