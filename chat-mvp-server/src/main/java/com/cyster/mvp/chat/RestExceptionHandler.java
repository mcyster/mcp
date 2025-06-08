package com.cyster.mvp.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(RestException.class)
  public ResponseEntity<RestErrorResponse> handle(RestException exception) {
    log.error(
        "RestException: {} {} {} {}",
        exception.getUniqueId(),
        exception.getErrorCode(),
        exception.getMessage(),
        exception.getParameters());

    return ResponseEntity.status(exception.getHttpStatusCode())
        .body(
            new RestErrorResponse(
                exception.getHttpStatusCode(),
                exception.getUniqueId(),
                exception.getErrorCode(),
                exception.getMessage(),
                exception.getParameters()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestErrorResponse> handleUnexpected(Exception exception) {
    log.error("Unhandled exception", exception);
    RestException wrapped =
        new RestException(
            null,
            "Internal server error",
            org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);

    RestErrorResponse body =
        new RestErrorResponse(
            wrapped.getHttpStatusCode(),
            wrapped.getUniqueId(),
            wrapped.getErrorCode(),
            wrapped.getMessage(),
            wrapped.getParameters());

    return ResponseEntity.status(wrapped.getHttpStatusCode()).body(body);
  }
}
