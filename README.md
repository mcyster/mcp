
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

### Ubuntu

Installing Java 21 and Gradle:

```bash
sudo apt-get install -y openjdk-21-jdk gradle locales
```

After installation ensure Java 21 is selected:

```bash
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

With Java and Gradle installed, run the standard Gradle commands from the
project root.

### Nix

If you prefer the full Nix environment install Nix and update channels:

```bash
nix-channel --add https://nixos.org/channels/nixpkgs-unstable nixpkgs
nix-channel --update
```

Then enter the shell:

```bash
nix-shell shell.nix
```

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

