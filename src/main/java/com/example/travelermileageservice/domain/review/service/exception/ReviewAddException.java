package com.example.travelermileageservice.domain.review.service.exception;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ReviewAddException extends BusinessException {

    private final Errors errors;

    public ReviewAddException(final Errors errors) {
        this.errors = errors;
    }

    public ReviewAddException(final String message, final Errors errors) {
        super(message);
        this.errors = errors;
    }
}
