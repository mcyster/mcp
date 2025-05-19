package com.cyster.mcp.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class McpTool {

    public static void main(String[] args) {
        Options opts = new Options();
        opts.addOption(Option.builder("t")
                             .longOpt("tools")
                             .desc("List all available MCP tools in JSON")
                             .build());
        opts.addOption(Option.builder("u")
                             .longOpt("url")
                             .hasArg()
                             .argName("URL")
                             .desc("Base URL of the MCP server (default: http://localhost:8080)")
                             .build());
        opts.addOption(Option.builder("h")
                             .longOpt("help")
                             .desc("Show this help message")
                             .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter help = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(opts, args);

            if (cmd.hasOption("h")) {
                help.printHelp("mcp-tool", opts, true);
                return;
            }

            boolean listTools = cmd.hasOption("t");
            String baseUrl = cmd.getOptionValue("u", "http://localhost:8080");

            // 2) build transport & client
            HttpClientSseClientTransport transport = HttpClientSseClientTransport
                .builder(baseUrl)
                .build();
            McpSyncClient client = McpClient.sync(transport).build();

            // 3) initialize (ping)
            McpSchema.InitializeResult init = client.initialize();
            if (!listTools) {
                System.out.printf("Ping successful, protocolVersion=%s%n",
                                  init.protocolVersion());
                return;
            }

            // 4) list tools
            McpSchema.ListToolsResult tools = client.listTools();
            ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
            String json = mapper.writeValueAsString(tools.tools());
            System.out.println(json);

        } catch (ParseException e) {
            System.err.println("Error Invalid arguments: " + e.getMessage());
            help.printHelp("mcp-tool", opts, true);
        } catch (JsonProcessingException e) {
            System.err.println("Error JSON error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error MCP error: " + e.getMessage());
        }
    }
}