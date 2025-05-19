package com.cyster.mcp.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

public class McpTool {

    public static void main(String[] args) throws JsonProcessingException {
        HttpClientSseClientTransport transport = HttpClientSseClientTransport
                .builder("http://localhost:8080")
                .build();  

        McpSyncClient client = McpClient
                .sync(transport)
                .build();                   

        client.initialize();
     
        McpSchema.ListToolsResult tools = client.listTools();
        
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        String jsonArray = mapper.writeValueAsString(tools.tools());
        System.out.println(jsonArray);

        /*
        McpSchema.CallToolRequest request = 
            new McpSchema.CallToolRequest(
                "getWeatherForecastByLocation",
                Map.of("latitude",  47.6062,
                       "longitude", -122.3321)
            );
        McpSchema.CallToolResult forecast = client.callTool(request);        
        System.out.println("Forecast: " + forecast);
        */
    }
}