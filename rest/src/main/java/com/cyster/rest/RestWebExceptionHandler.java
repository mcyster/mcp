package com.cyster.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class RestWebExceptionHandler implements ErrorWebExceptionHandler {

  private final ObjectMapper objectMapper;

  public RestWebExceptionHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
    if (throwable instanceof RestException restException) {
      return handleRestException(exchange, restException);
    }

    if (throwable instanceof ResponseStatusException exception
        && exception.getStatusCode() == HttpStatus.NOT_FOUND) {
      RestException restException =
          new RestException(
              GlobalErrorCode.NOT_FOUND, "Endpoint not defined", HttpStatus.NOT_FOUND);
      return handleRestException(exchange, restException);
    }

    return Mono.error(throwable);
  }

  private Mono<Void> handleRestException(ServerWebExchange exchange, RestException restException) {
    try {
      RestException.RestExceptionResponse errorResponse = restException.toRestExceptionResponse();
      String responseBody = objectMapper.writeValueAsString(errorResponse);

      DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
      DataBuffer buffer = bufferFactory.wrap(responseBody.getBytes());

      exchange.getResponse().setStatusCode(HttpStatus.valueOf(restException.httpStatusCode()));
      exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

      return exchange.getResponse().writeWith(Mono.just(buffer));
    } catch (Exception jsonException) {
      return Mono.error(jsonException);
    }
  }
}
