package com.cyster.mcp;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport;
import io.netty.handler.logging.LogLevel;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

public class McpTestSse {

    public static void main(String[] args) {

        HttpClient tcpClient = HttpClient.create().wiretap(HttpClient.class.getName(), LogLevel.DEBUG,
                AdvancedByteBufFormat.TEXTUAL, java.nio.charset.StandardCharsets.UTF_8);

        ExchangeFilterFunction logRequest = ExchangeFilterFunction.ofRequestProcessor(request -> {
            //System.out.println("➡️ " + request.method() + " " + request.url());
            //request.headers().forEach((n, vs) -> vs.forEach(v -> System.out.println(n + ": " + v)));
            return Mono.just(request);
        });

        ExchangeFilterFunction logResponse = ExchangeFilterFunction.ofResponseProcessor(response -> {
            //System.out.println("⬅️ HTTP " + response.statusCode());
            //response.headers().asHttpHeaders().forEach((n, vs) -> vs.forEach(v -> System.out.println(n + ": " + v)));
            return Mono.just(response);
        });

        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl("http://localhost:8080")
                .clientConnector(new ReactorClientHttpConnector(tcpClient))
                .filters(filter -> {
                    filter.add(logRequest);
                    filter.add(logResponse);
                });

        WebFluxSseClientTransport transport = new WebFluxSseClientTransport(webClientBuilder);

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
