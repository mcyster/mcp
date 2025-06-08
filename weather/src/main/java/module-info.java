module com.cyster.weather {
  requires spring.web;
  requires spring.beans;
  requires spring.context;
  requires com.fasterxml.jackson.databind;

  exports com.cyster.weather.service;

  opens com.cyster.weather.impl to
      spring.core,
      spring.beans,
      spring.context;
}
