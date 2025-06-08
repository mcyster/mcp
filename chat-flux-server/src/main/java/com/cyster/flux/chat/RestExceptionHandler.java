package com.cyster.flux.chat;

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
        exception.uniqueId(),
        exception.errorCode(),
        exception.getMessage(),
        exception.parameters());

    return ResponseEntity.status(exception.httpStatusCode())
        .body(
            new RestErrorResponse(
                exception.httpStatusCode(),
                exception.uniqueId(),
                exception.errorCode(),
                exception.getMessage(),
                exception.parameters()));
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
            wrapped.httpStatusCode(),
            wrapped.uniqueId(),
            wrapped.errorCode(),
            wrapped.getMessage(),
            wrapped.parameters());

    return ResponseEntity.status(wrapped.httpStatusCode()).body(body);
  }
}
