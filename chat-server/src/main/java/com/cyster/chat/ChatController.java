package com.cyster.chat;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;

import java.util.List;
import java.util.Arrays;

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
    public Mono<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request.prompt() == null || request.prompt().trim().isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, 
            "No prompt was specified"));
        }

        return chatClient.prompt()
            .user(request.prompt())
            .toolCallbacks(Arrays.asList(toolCallbackProvider.getToolCallbacks()))
            .stream()
            .content()
            .collectList()
            .map(list -> new ChatResponse(String.join("", list)));
    }

    @GetMapping("/tools")
    public List<String> tools() {
        return Arrays.stream(toolCallbackProvider.getToolCallbacks())
            .map(toolCallback -> toolCallback.getToolDefinition().name())
            .toList();
    }

    public record ChatRequest(String prompt) {};
    public record ChatResponse(String response) {};
    public static record ChatErrorResponse(ChatErrorCode code, String message) {}
    public static enum ChatErrorCode {
        PROMPT_MISSING,
        TOOL_FAILURE
    }

    public static class ChatException extends RuntimeException {
        private final ChatErrorCode code;
        private final HttpStatus status;

        public ChatException(ChatErrorCode code, String message) {
            this(code, message, HttpStatus.BAD_REQUEST);
        }

        public ChatException(ChatErrorCode code, String message, HttpStatus status) {
            super(message);
            this.code = code;
            this.status = status;
        }

        public ChatErrorCode getCode() {
            return code;
        }

        public HttpStatus getStatus() {
            return status;
        }
    }
}

