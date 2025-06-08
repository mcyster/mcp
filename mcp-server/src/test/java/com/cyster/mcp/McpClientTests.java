package com.cyster.mcp;

import static org.junit.jupiter.api.Assertions.*;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class McpClientTests {

  @LocalServerPort private int port;

  private McpSyncClient client;

  @BeforeEach
  void setUp() {
    HttpClient tcpClient = HttpClient.create();
    WebClient.Builder builder =
        WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(tcpClient))
            .baseUrl("http://localhost:" + port);
    var transport = new WebFluxSseClientTransport(builder);
    client = io.modelcontextprotocol.client.McpClient.sync(transport).build();
    client.initialize();
    client.ping();
  }

  @AfterEach
  void tearDown() {
    client.close();
  }

  @Test
  void listAndCallTools() {
    ListToolsResult tools = client.listTools();
    assertNotNull(tools);
    assertFalse(tools.tools().isEmpty());

    CallToolResult weather =
        client.callTool(
            new CallToolRequest(
                "getWeatherForecastByLocation",
                Map.of("latitude", "47.6062", "longitude", "-122.3321")));
    assertNotNull(weather);

    CallToolResult alert = client.callTool(new CallToolRequest("getAlerts", Map.of("state", "NY")));
    assertNotNull(alert);
  }
}
