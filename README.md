
# Spring MCP Server

Working against Spring AI v1.0.0

- https://docs.spring.io/spring-ai/reference/1.0/index.html
  - https://github.com/spring-projects/spring-ai/tree/v1.0.0
  - https://github.com/modelcontextprotocol/servers

Repo
- [mcp-server](./mcp-server)
  - MCP server built using Spring AI
- [chat-server](./chat-server)
  - Chat server built using Spring AI
- [mcp-tool](./mcp-tool)
  - Tool to make MCP tool requests, to help understand the protocol

## Setup

### Ubuntu Setup

If you are working under Ubuntu install the required packages:

```bash
sudo apt-get update
sudo apt-get install -y openjdk-21-jdk gradle locales
sudo locale-gen en_US.UTF-8
sudo update-locale LANG=en_US.UTF-8 LC_ALL=en_US.UTF-8
```

Ensure Java 21 is configured as the default JDK and run `gradle` commands directly from the project directory.

### Common Gradle commands

```bash
gradle test
gradle :mcp-app:bootJar
gradle :mcp-server:runMcpTestSse
gradle :mcp-server:runMcpTestStdio
gradle spotlessCheck  # verify formatting
gradle spotlessApply  # automatically format sources
```

## Configuration

The `mcp-server` module reads a `tool-context.home` property to populate
environment information returned from `ToolContextService`. The default
`application.yml` maps this property to the `TOOL_CONTEXT_HOME` environment
variable falling back to `HOME`:

```yaml
tool-context:
  home: ${TOOL_CONTEXT_HOME:${HOME}}
```

You can override this value using Spring configuration or by exporting the
`TOOL_CONTEXT_HOME` variable when running the server.

## License

This project is licensed under the [MIT License](./LICENSE).

