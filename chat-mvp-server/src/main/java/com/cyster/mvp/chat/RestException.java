package com.cyster.mvp.chat;

import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.UUID;

public class RestException<CODE extends Enum<CODE>> extends Exception implements ErrorResponse<CODE> {
    private final CODE errorCode;
    private final HttpStatus status;
    private final Map<String, Object> parameters;

    public RestException(CODE errorCode, String message, HttpStatus status, Map<String, Object> parameters) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.parameters = parameters;
    }

    public RestException(CODE errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.parameters = Map.of();
    }

    public RestException(CODE errorCode, String message) {
        this(errorCode, message, HttpStatus.BAD_REQUEST, Map.of());
    }

    @Override
    public String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public CODE getErrorCode() {
        return errorCode;
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return status;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }
} 
