
# Spring AI Chat Server

Start server:
```
cd $MCP_HOME

gradle :char-server:bootRun
```

Prompt server:
```
curl -X POST http://localhost:9000/chat -H "Content-Type: application/json" -d '{"userInput":"Hello, how are you?"}'
```

## Plan 

- make MCP Client
- expose Chat as MCP Tool

