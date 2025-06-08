package com.cyster.mvp.chat;

import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.UUID;

public class RestException<E extends Enum<?>> extends Exception implements ErrorResponse<E> {
    private final E errorCode;
    private final HttpStatus status;
    private final Map<String, Object> parameters;

    public RestException(E errorCode, String message, HttpStatus status, Map<String, Object> parameters) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.parameters = parameters;
    }

    public RestException(E errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.parameters = Map.of();
    }

    public RestException(E errorCode, String message) {
        this(errorCode, message, HttpStatus.BAD_REQUEST, Map.of());
    }

    @Override
    public String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public E getErrorCode() {
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
