module com.cyster.weather.tool {
  requires com.cyster.weather;
  requires spring.ai.model;
  requires spring.context;

  exports com.cyster.weather.tool;

  opens com.cyster.weather.tool to
      spring.core,
      spring.beans,
      spring.context;
}
