package com.cyster.mvp.chat;

import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.UUID;

public class RestException extends Exception {
    private final Enum<?> errorCode;
    private final HttpStatus status;
    private final Map<String, Object> parameters;

    public RestException(Enum<?> errorCode, String message, HttpStatus status, Map<String, Object> parameters) {
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

    public String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public Enum<?> getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatusCode() {
        return status;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
} 
