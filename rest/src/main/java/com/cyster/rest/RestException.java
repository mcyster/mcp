package com.cyster.rest;

import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException implements ErrorResponse {
  private final String uniqueId;
  private final Enum<?> errorCode;
  private final HttpStatus status;
  private final Map<String, Object> parameters;

  public RestException(ErrorResponse errorResponse) {
    super(errorResponse.message());
    this.uniqueId = errorResponse.uniqueId();
    this.errorCode = errorResponse.errorCode();
    this.status = HttpStatus.valueOf(errorResponse.httpStatusCode());
    this.parameters = errorResponse.parameters();
  }

  public RestException(
      Enum<?> errorCode, String message, HttpStatus status, Map<String, Object> parameters) {
    super(message);
    this.uniqueId = UUID.randomUUID().toString();
    this.errorCode = errorCode;
    this.status = status;
    this.parameters = parameters;
  }

  public RestException(Enum<?> errorCode, String message, HttpStatus status) {
    super(message);
    this.uniqueId = UUID.randomUUID().toString();
    this.errorCode = errorCode;
    this.status = status;
    this.parameters = Map.of();
  }

  public RestException(Enum<?> errorCode, String message) {
    this(errorCode, message, HttpStatus.BAD_REQUEST, Map.of());
  }

  @Override
  public String uniqueId() {
    return uniqueId;
  }

  @Override
  public Enum<?> errorCode() {
    return errorCode;
  }

  @Override
  public int httpStatusCode() {
    return status.value();
  }

  @Override
  public Map<String, Object> parameters() {
    return parameters;
  }

  @Override
  public String message() {
    return getMessage();
  }

  public RestExceptionResponse toRestExceptionResponse() {
    return new RestExceptionResponse(uniqueId, errorCode, getMessage(), parameters);
  }

  public record RestExceptionResponse(
      String uniqueId, Enum<?> errorCode, String message, Map<String, Object> parameters) {}
}
