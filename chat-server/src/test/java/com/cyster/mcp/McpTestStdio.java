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

        new McpTestClient(transport).run();
    }

}
