# Simple Spring MCP Server 

## [Cursor](https://www.cursor.com) Integration

Start server:
```
cd $MCP_HOME

gradle :mcp-server:bootRun
```

Setup an MCP entry in cursor:

$MCP_HOME/.cursor/mcp.json
```
{
  "mcpServers": {
    "mcp-server": {
      "url": "http://localhost:8080/sse"
    }
  }
}
```

Restart the server:
- Cursor Settings
  - MCP
    - click the restart button on the server

Then in cusor, ask something like:
> Hi what is the weather in San Francisco

You will have to:
- Approve the tool to run

Then I got the answer:
> The current weather in San Francisco is mostly sunny with a high temperature near 63 degrees Fahrenheit. The wind is coming from the west at around 15 miles per hour, with gusts as high as 23 miles per hour. Tonight, it will be mostly cloudy with a low temperature around 53 degrees Fahrenheit and west wind between 6 to 14 miles per hour.
 > If you need a detailed forecast for the coming days, let me know.

If you have problems, increase the log level in the app `$MCP_HOME/mcp-server/src/main/resources/application.properties` to DEBUG

## Testing

###

```
gradle :mcp-server:runMcpTestSse
```
works

```
gradle :mcp-server:runMcpTestStdio
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

 
