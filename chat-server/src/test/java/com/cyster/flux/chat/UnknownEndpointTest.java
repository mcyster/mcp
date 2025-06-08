package com.cyster.flux.chat;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.cyster.rest.RestWebExceptionHandler;

@WebFluxTest(ChatController.class)
@Import(RestWebExceptionHandler.class)
class UnknownEndpointTest {

  @Autowired private WebTestClient webTestClient;

  @MockitoBean ChatModel chatModel;

  @MockitoBean ToolCallbackProvider toolCallbackProvider;

  @Test
  void unknownEndpointReturnsNotFoundRestException() {
    webTestClient
        .get()
        .uri("/tools")
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.httpStatusCode")
        .isEqualTo(404)
        .jsonPath("$.errorCode")
        .isEqualTo("NOT_FOUND")
        .jsonPath("$.message")
        .isEqualTo("Endpoint not defined");
  }
}
