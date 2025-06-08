package com.cyster.flux.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
public class ChatController {

  private final ChatClient chatClient;
  private final ToolCallbackProvider toolCallbackProvider;

  public ChatController(ChatModel chatModel, ToolCallbackProvider toolCallbackProvider) {
    this.toolCallbackProvider = toolCallbackProvider;
    this.chatClient = ChatClient.create(chatModel);
  }

  @PostMapping
  @Operation(summary = "Send a chat message")
  public Mono<ChatResult> chat(@RequestBody ChatRequest request) {
    if (request.prompt() == null || request.prompt().trim().isEmpty()) {
      return Mono.error(
          new RestException(
              new ChatErrorResponse(
                  400,
                  java.util.UUID.randomUUID().toString(),
                  ChatErrorCode.PROMPT_MISSING,
                  "No prompt was specified",
                  java.util.Map.of())));
    }

    return chatClient
        .prompt()
        .user(request.prompt())
        .toolCallbacks(Arrays.asList(toolCallbackProvider.getToolCallbacks()))
        .stream()
        .content()
        .collectList()
        .map(list -> (ChatResult) new ChatResponse(String.join("", list)))
        .onErrorMap(
            throwable ->
                new RestException(
                    new ChatErrorResponse(
                        400,
                        java.util.UUID.randomUUID().toString(),
                        ChatErrorCode.TOOL_FAILURE,
                        "Chat processing failed: " + throwable.getMessage(),
                        java.util.Map.of())));
  }

  @GetMapping("/tools")
  @Operation(summary = "List available tools")
  public List<String> tools() {
    return Arrays.stream(toolCallbackProvider.getToolCallbacks())
        .map(toolCallback -> toolCallback.getToolDefinition().name())
        .toList();
  }

  @Schema(description = "Chat request containing user prompt")
  public record ChatRequest(
      @Schema(description = "User message or question", example = "What is the weather like today?")
          String prompt) {}
  ;

  @Schema(oneOf = {ChatResponse.class, ChatErrorResponse.class})
  public sealed interface ChatResult permits ChatResponse, ChatErrorResponse {}

  @Schema(description = "Chat response containing AI-generated content")
  public record ChatResponse(@Schema(description = "AI-generated response text") String response)
      implements ChatResult {}
  ;

  @Schema(description = "Chat error response for validation and processing errors")
  public static record ChatErrorResponse(
      @Schema(description = "HTTP status code of the error") int httpStatusCode,
      @Schema(description = "Unique identifier for tracking the error") String uniqueId,
      @Schema(description = "Specific error code indicating the type of error")
          ChatErrorCode errorCode,
      @Schema(description = "Human-readable error message") String message,
      @Schema(description = "Additional information about the error")
          Map<String, Object> parameters)
      implements ChatResult, ErrorResponse {}

  public static enum ChatErrorCode {
    @Schema(description = "Prompt was empty or missing")
    PROMPT_MISSING,

    @Schema(description = "Tool call failed")
    TOOL_FAILURE
  }
}
