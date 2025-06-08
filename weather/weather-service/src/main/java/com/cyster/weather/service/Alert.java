package com.cyster.weather.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

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
