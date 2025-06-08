package com.cyster.weather.service;

public record Points(Properties properties) {
    public record Properties(String forecast) {
    }
}
