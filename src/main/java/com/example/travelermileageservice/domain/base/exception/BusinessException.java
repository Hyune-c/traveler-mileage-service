package com.example.travelermileageservice.domain.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {

    private final Errors errors;
}
