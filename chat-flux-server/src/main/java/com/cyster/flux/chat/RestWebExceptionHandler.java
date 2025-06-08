package com.cyster.flux.chat;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        
        return Mono.error(throwable);
    }

    private Mono<Void> handleRestException(ServerWebExchange exchange, RestException restException) {
        try {
            RestException.RestExceptionResponse errorResponse = restException.toRestExceptionResponse();
            String responseBody = objectMapper.writeValueAsString(errorResponse);
            
            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            DataBuffer buffer = bufferFactory.wrap(responseBody.getBytes());
            
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception jsonException) {
            return Mono.error(jsonException);
        }
    }
} 