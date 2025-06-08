package com.cyster.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<RestErrorResponse> handleResponseStatus(ResponseStatusException exception) {
    if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
      return handle(
          new RestException(
              GlobalErrorCode.NOT_FOUND, "Endpoint not defined", HttpStatus.NOT_FOUND));
    }

    return handle(
        new RestException(
            null, exception.getReason(), HttpStatus.valueOf(exception.getStatusCode().value())));
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
