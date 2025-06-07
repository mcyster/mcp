package com.cyster.mcp;

import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;

public class McpTestStdio {

    public static void main(String[] args) {

        var stdioParams = ServerParameters.builder("java")
                .args("-Dspring.ai.mcp.server.stdio=true", "-Dspring.main.web-application-type=none",
                        "-Dlogging.pattern.console=", "-jar",
                        "build/libs/mcp-app.jar")
                .build();

        var transport = new StdioClientTransport(stdioParams);

        var client = io.modelcontextprotocol.client.McpClient.sync(transport).build();
        client.initialize();
        client.ping();
        var tools = client.listTools();
        System.out.println("Available Tools = " + tools);
        var weather = client.callTool(new io.modelcontextprotocol.spec.McpSchema.CallToolRequest(
                "getWeatherForecastByLocation",
                java.util.Map.of("latitude", "47.6062", "longitude", "-122.3321")));
        System.out.println("Weather Forecast: " + weather);
        var alert = client.callTool(new io.modelcontextprotocol.spec.McpSchema.CallToolRequest(
                "getAlerts", java.util.Map.of("state", "NY")));
        System.out.println("Alert Response = " + alert);
        client.close();
    }

}
