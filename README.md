
# Spring MCP Server 

Working against RC1

## Testing

### SSE

Print incoming events (-N no buffer)
```
 curl -N -H 'Accept: text/event-stream'  http://localhost:8080/sse
```

gives a response like
```
event:endpoint
data:/mcp/message?sessionId=2d9ffdbd-b022-487e-8a94-236c0ecb3f62
``` 
 
# Stdio

Build mcp-app.jar
```
cd mcp
nix-shell
gradle :mcp-app:bootJar
```

create some commands
```
cat <<EOF > requests.json
{"jsonrpc":"2.0","id":1,"method":"tools/list","params":{}}
{"jsonrpc":"2.0","id":2,"method":"tools/call","params":{"name":"weather","arguments":{"location":"London"}}}
EOF
```

```
cat requests.json | java -jar $MCP_HOME/mcp-app/build/libs/mcp-app.jar --spring.main.web-application-type=none --spring.ai.mcp.server.stdio=true --spring.main.banner-mode=off
```
Currently this is starting a server and not processing the commands

 
## References
- https://docs.spring.io/spring-ai/reference/1.0/index.html

