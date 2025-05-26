package com.cyster.mcp;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.ai.mcp.McpToolUtils;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import java.util.Optional;

@Service
public class EnvironmentService {
   
    @Tool(description = "Get User Environment")
    public String getUserEnvironment(ToolContext toolContext) {
        Optional<McpSyncServerExchange> exchangeOpt = McpToolUtils.getMcpExchange(toolContext);

        if (exchangeOpt.isEmpty()) {
            return "No MCP session";
        }

        McpSyncServerExchange exchange = exchangeOpt.get();
        // now you have the McpServerSession under the hood
        String clientInfo = exchange.getClientInfo().toString();

        return "ClientInfo: " + clientInfo;
    }
}