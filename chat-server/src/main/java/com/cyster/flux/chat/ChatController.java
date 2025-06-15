package com.cyster.flux.chat;

import com.cyster.rest.ErrorResponse;
import com.cyster.rest.RestException;
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
  public Mono<ChatResult> chat(
      @RequestBody ChatRequest request,
      @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
    String bearerToken =
        authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
            ? authorizationHeader.substring("Bearer ".length())
            : "invalid";
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
        .toolContext(java.util.Map.of("bearerToken", bearerToken))
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
  public List<String> tools() {
    return Arrays.stream(toolCallbackProvider.getToolCallbacks())
        .map(toolCallback -> toolCallback.getToolDefinition().name())
        .toList();
  }

  public record ChatRequest(String prompt) {}

  public sealed interface ChatResult permits ChatResponse, ChatErrorResponse {}

  public record ChatResponse(String response) implements ChatResult {}

  public static record ChatErrorResponse(
      int httpStatusCode,
      String uniqueId,
      ChatErrorCode errorCode,
      String message,
      Map<String, Object> parameters)
      implements ChatResult, ErrorResponse<ChatErrorCode> {}

  public static enum ChatErrorCode {
    PROMPT_MISSING,
    TOOL_FAILURE
  }
}
