package com.example.travelermileageservice.domain.base.exception;

public class BusinessException extends RuntimeException {

    public BusinessException() {
    }

    public BusinessException(final String message) {
        super(message);
    }
}
