package com.cyster.mvp.chat;

import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;

public class RestException extends Exception implements ErrorResponse {
  private final Enum<?> errorCode;
  private final HttpStatus status;
  private final Map<String, Object> parameters;

  public RestException(
      Enum<?> errorCode, String message, HttpStatus status, Map<String, Object> parameters) {
    super(message);
    this.errorCode = errorCode;
    this.status = status;
    this.parameters = parameters;
  }

  public RestException(Enum<?> errorCode, String message, HttpStatus status) {
    super(message);
    this.errorCode = errorCode;
    this.status = status;
    this.parameters = Map.of();
  }

  public RestException(Enum<?> errorCode, String message) {
    this(errorCode, message, HttpStatus.BAD_REQUEST, Map.of());
  }

  @Override
  public String getUniqueId() {
    return UUID.randomUUID().toString();
  }

  @Override
  public Enum<?> getErrorCode() {
    return errorCode;
  }

  @Override
  public int getHttpStatusCode() {
    return status.value();
  }

  @Override
  public Map<String, Object> getParameters() {
    return parameters;
  }
}
