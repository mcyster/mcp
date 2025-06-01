
# Spring AI Chat Server

Start server:
```
cd $MCP_HOME

./gradlew :chat-server:bootRun
```

Prompt server:
```
curl -s -H "Content-Type: application/json" http://localhost:9000/chat -d '{"prompt":"Whats the weather in San Francisco"}' | jq -r .response

```

Tools avaliable to chat
```
curl -s  -H "Content-Type: application/json" http://localhost:9000/chat/tools | jq .
```


## Plan 

- make MCP Client
- expose Chat as MCP Tool

