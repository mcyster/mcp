package com.cyster.mvp.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
  public ChatResponse chat(@RequestBody ChatRequest request) throws ChatException {
    if (request.prompt() == null || request.prompt().trim().isEmpty()) {
      throw new ChatException(ChatErrorCode.PROMPT_MISSING, "No prompt was specified");
    }

    try {
      String response =
          chatClient
              .prompt()
              .user(request.prompt())
              .toolCallbacks(Arrays.asList(toolCallbackProvider.getToolCallbacks()))
              .call()
              .content();
      return new ChatResponse(response);
    } catch (Exception exception) {
      throw new ChatException(
          ChatErrorCode.TOOL_FAILURE, "Chat processing failed: " + exception.getMessage());
    }
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

  @Schema(description = "Chat response containing AI-generated content")
  public record ChatResponse(@Schema(description = "AI-generated response text") String response) {}

  @Schema(description = "Chat error response for validation and processing errors")
  public static record ChatErrorResponse(
      @Schema(description = "Specific error code indicating the type of error") ChatErrorCode code,
      @Schema(description = "Human-readable error message") String message) {}

  public static enum ChatErrorCode {
    @Schema(description = "Prompt was empty or missing")
    PROMPT_MISSING,

    @Schema(description = "Tool call failed")
    TOOL_FAILURE
  }

  public class ChatException extends RestException {
    public ChatException(ChatErrorCode code, String message) {
      super(code, message, HttpStatus.BAD_REQUEST);
    }
  }
}
