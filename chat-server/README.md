
# Spring AI Chat Server

Start server:
```
cd $MCP_HOME

gradle :chat-server:bootRun
```

Prompt server:
```
curl -s -H "Content-Type: application/json" http://localhost:8000/chat -d '{"prompt":"Whats the weather in San Francisco"}' | jq -r .response

```

Tools avaliable to chat
```
curl -s  -H "Content-Type: application/json" http://localhost:8000/chat/tools | jq .
```

When the server is running check the api:
- [Swagger Description](http://localhost:8000/swagger-ui.html)
- [ApI Docs](http://localhost:8000/v3/api-docs)


