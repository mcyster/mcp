package com.cyster.flux.chat;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(ChatController.class)
@Import(RestExceptionHandler.class)
class ChatExceptionHandlerTest {

  @Autowired private WebTestClient webTestClient;

  @MockitoBean ChatModel chatModel;

  @MockitoBean ToolCallbackProvider toolCallbackProvider;

  @Test
  void missingPromptReturnsErrorResponse() {
    webTestClient
        .post()
        .uri("/chat")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{\"prompt\":\"\"}")
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.httpStatusCode")
        .isEqualTo(400)
        .jsonPath("$.errorCode")
        .isEqualTo("PROMPT_MISSING")
        .jsonPath("$.message")
        .isEqualTo("No prompt was specified");
  }
}
