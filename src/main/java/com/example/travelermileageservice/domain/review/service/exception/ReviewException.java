package com.example.travelermileageservice.domain.review.service.exception;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ReviewException extends BusinessException {

    public ReviewException(final Errors errors) {
        super(errors);
    }

    public ReviewException(final String message) {
        super(message);
    }
}
