module com.cyster.weather.tool {
    requires com.cyster.weather;
    requires org.springframework.ai.autoconfigure.model.tool;
    requires spring.context;

    exports com.cyster.weather.tool;
    opens com.cyster.weather.tool to spring.core, spring.beans, spring.context;
}
