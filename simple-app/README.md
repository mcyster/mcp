
# Simple Spring MCP Server 

## Testing

###

```
gradle :simple-app:runMcpTestSse
```

```
gradle :simple-app:runMcpTestStdio
```
currently errors - TBD

### Command line SSE

Print incoming events (-N no buffer)
```
 curl -N -H 'Accept: text/event-stream'  http://localhost:8080/sse
```

gives a response like
```
event:endpoint
data:/mcp/message?sessionId=2d9ffdbd-b022-487e-8a94-236c0ecb3f62
``` 

In another terminal, grab the session id and define an environment variable
```
session_id=2d9ffdbd-b022-487e-8a94-236c0ecb3f62
```

Initialize
```
curl -i -X POST \
  "http://localhost:8080/mcp/message?sessionId=$session_id" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc":"2.0",
    "method":"initialize",
    "id":"1",
    "params":{
      "protocolVersion":"2024-11-05",
      "capabilities":{},
      "clientInfo":{
        "name":"Java SDK MCP Client",
        "version":"1.0.0"
      }
    }
  }'
 ```
 
 
 Notifications/initialized
 ```
curl -i -X POST \
  "http://localhost:8080/mcp/message?sessionId=$session_id" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"notifications/initialized"}'
```


Ping
```
curl -i -X POST \
  "http://localhost:8080/mcp/message?sessionId=$session_id" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"ping","id":"2"}'
```


List tools
```
curl -i -X POST \
  "http://localhost:8080/mcp/message?sessionId=$session_id" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc":"2.0",
    "method":"tools/list",
    "id":"3",
    "params":{}
  }'
```

Run the tool `getWeatherForecastByLocation`
```
curl -i -X POST \
  "http://localhost:8080/mcp/message?sessionId=$session_id" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc":"2.0",
    "method":"tools/call",
    "id":"4",
    "params":{
      "name":"getWeatherForecastByLocation",
      "arguments":{
        "latitude":47.6062,
        "longitude":-122.3321
      }
    }
  }'
```

Get Alerts
```
curl -i -X POST \
  "http://localhost:8080/mcp/message?sessionId=$session_id" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc":"2.0",
    "method":"tools/call",
    "id":"5",
    "params":{
      "name":"getAlerts",
      "arguments":{
        "state":"NY"
      }
    }
  }'
```

These commands where derived by running `gradle :mcp-app:runMcpTestSse` and asking ChatGPT to convert the debug output to curls.

### Command line Stdio

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
{"jsonrpc":"2.0","id":2,"method":"tools/call","params":{"name":"getWeatherForecastByLocation","arguments":{"latitude": 47.6062, "longitude": -122.3321}}}
EOF
```

```
cat requests.json | java -jar $MCP_HOME/mcp-app/build/libs/mcp-app.jar --spring.main.web-application-type=none --spring.ai.mcp.server.stdio=true --spring.main.banner-mode=off
```
Currently this is starting a server and not processing the commands

 
