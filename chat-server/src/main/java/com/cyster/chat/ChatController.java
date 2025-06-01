package com.cyster.chat;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    @PostMapping
    public Mono<ChatResponse> chat(@RequestBody ChatRequest request) {
        return chatClient.prompt()
            .user(request.prompt())
            .stream()
            .content()
            .collectList()
            .map(list -> new ChatResponse(String.join("", list)));
    }

    public record ChatRequest(String prompt) {};
    public record ChatResponse(String response) {};
}

