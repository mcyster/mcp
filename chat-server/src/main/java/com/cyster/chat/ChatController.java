package com.cyster.chat;

import org.springframework.web.bind.annotation.*;
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
}

