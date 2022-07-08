package com.example.travelermileageservice.config.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class CustomValidationException extends RuntimeException {

    private final BindingResult bindingResult;

    public CustomValidationException(final BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
