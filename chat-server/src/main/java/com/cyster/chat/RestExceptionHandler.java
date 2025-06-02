package com.cyster.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    public ResponseEntity<ChatController.ChatErrorResponse> handleChatException(ChatController.ChatException exception) {
        log.error("ChatException: {} {}", exception.getErrorCode(), exception.getMessage());
        
        ChatController.ChatErrorCode errorCode = (ChatController.ChatErrorCode) exception.getErrorCode();
        ChatController.ChatErrorResponse errorResponse = new ChatController.ChatErrorResponse(errorCode, exception.getMessage());
        
        return ResponseEntity
            .status(exception.getHttpStatusCode())
            .body(errorResponse);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<RestErrorResponse> handle(RestException exception) {
        log.error("RestException: {} {} {} {}", exception.getUniqueId(), exception.getErrorCode(), exception.getMessage(), exception.getParameters());
        
        return ResponseEntity
            .status(exception.getHttpStatusCode())
            .body(new RestErrorResponse(exception.getHttpStatusCode().value(), exception.getUniqueId(), exception.getErrorCode(), exception.getMessage(), exception.getParameters()));
    }

    public record RestErrorResponse(int httpStatusCode, String uniqueId, Enum<?> code, String message, Map<String, Object> parameters) {}
}