package com.cyster.flux.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.*;
import com.cyster.flux.chat.RestErrorResponse;
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
              ChatErrorCode.PROMPT_MISSING,
              "No prompt was specified",
              org.springframework.http.HttpStatus.BAD_REQUEST));
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
                    ChatErrorCode.TOOL_FAILURE,
                    "Chat processing failed: " + throwable.getMessage(),
                    org.springframework.http.HttpStatus.BAD_REQUEST));
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

  @Schema(oneOf = {ChatResponse.class, RestErrorResponse.class})
  public sealed interface ChatResult permits ChatResponse, RestErrorResponse {}

  @Schema(description = "Chat response containing AI-generated content")
  public record ChatResponse(@Schema(description = "AI-generated response text") String response)
      implements ChatResult {}
  ;


  public static enum ChatErrorCode {
    @Schema(description = "Prompt was empty or missing")
    PROMPT_MISSING,

    @Schema(description = "Tool call failed")
    TOOL_FAILURE
  }
}
