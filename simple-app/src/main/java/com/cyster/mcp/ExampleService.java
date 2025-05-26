package com.cyster.mcp;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import io.modelcontextprotocol.spec.McpServerSession;

//@Service
public class ExampleService {
   
    //@Tool(description = "Get MCP SSE Context")
    public String getContext(McpServerSession session  ) {
        // essionContext session = context.session();
        //String apiKey = session.env().get("API_KEY");

        return "hello";
    }
}