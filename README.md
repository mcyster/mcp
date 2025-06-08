
# Spring MCP Server

Working against Spring AI v1.0.0

- https://docs.spring.io/spring-ai/reference/1.0/index.html
  - https://github.com/spring-projects/spring-ai/tree/v1.0.0
  - https://github.com/modelcontextprotocol/servers

Repo
- [mcp-server](./mcp-server)
  - MCP server built using Spring AI
- [chat-flux-server](./chat-flux-server)
  - Chat server built using Spring AI
- [mcp-tool](./mcp-tool)
  - Tool to make MCP tool requests, to help understand the protocol

## Setup

1. If not already on a nix environment, install Nix and update channels (only needed once per environment):

```bash
apt-get update && apt-get install -y nix-bin 
nix-channel --add https://nixos.org/channels/nixpkgs-unstable nixpkgs
nix-channel --update
```

2. Enter the development environment:

```bash
nix-shell shell.nix
```

All `gradle` commands must be run inside this shell.

### Common Gradle commands

```bash
gradle test
gradle :mcp-app:bootJar
gradle :mcp-server:runMcpTestSse
gradle :mcp-server:runMcpTestStdio
gradle spotlessApply   # formats source code using google-java-format
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

