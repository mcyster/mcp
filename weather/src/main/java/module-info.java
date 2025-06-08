module com.cyster.weather {
    requires spring.web;
    requires spring.context;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    exports com.cyster.weather.service;
    exports com.cyster.weather.impl;
}
