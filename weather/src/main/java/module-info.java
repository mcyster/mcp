module com.cyster.weather {
    requires spring.web;
    requires spring.beans;
    requires spring.context;
    requires org.springframework.ai.model;
    requires com.fasterxml.jackson.databind;

    exports com.cyster.weather.service;
    opens com.cyster.weather.impl to spring.core, spring.beans, spring.context;
}
