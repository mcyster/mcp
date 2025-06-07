# Spring MVC Chat Server

Start server:
```bash
cd $MCP_HOME

gradle :chat-mvp-server:bootRun
```

Prompt server:
```
curl -s -H "Content-Type: application/json" http://localhost:9000/chat -d '{"prompt":"Whats the weather in San Francisco"}' | jq -r .response

```

Tools avaliable to chat
```
curl -s  -H "Content-Type: application/json" http://localhost:9000/chat/tools | jq .
```

When the server is running check the api:
- [Swagger Description](http://localhost:9000/webjars/swagger-ui/index.html)
- [ApI Docs](http://localhost:9000/v3/api-docs)


