package com.cyster.mcp;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.ai.mcp.McpToolUtils;
import io.modelcontextprotocol.server.McpSyncServerExchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ToolContextService {
   
    private static final Logger logger = LoggerFactory.getLogger(ToolContextService.class);

    @Tool(description = "Get the Model Context Protocol (MCP) Tool Context")
    public String getToolContext(ToolContext toolContext) {
        McpSyncServerExchange exchange = McpToolUtils.getMcpExchange(toolContext)
            .orElseThrow(() -> new IllegalArgumentException("No Exchange Server"));

        logger.info("ToolContext: {}", toolContext.getContext());

        logger.info("ToolContext.exchange.clientCapabilities: {}", exchange.getClientCapabilities());

        logger.info("ToolContext.exchange.clientInfo: {}", exchange.getClientInfo());

        String clientInfo = exchange.getClientInfo().toString();

        // need get session from model context protocol, looks like this will be in the next release of the MCP java-sdk 0.11.0
        // https://chatgpt.com/share/6834c998-1d3c-8008-81c9-70010740fd4e
        // McpServerSession session = exchange.getSession();
        // session.env();

        return "ClientInfo: " + clientInfo;
    }
}
