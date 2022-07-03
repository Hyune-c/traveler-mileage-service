package com.example.travelermileageservice.domain.review.service;

import com.example.travelermileageservice.domain.review.service.dto.ReviewModDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.Validation;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReviewModValidator implements Validator {

    private static final javax.validation.Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private static final String ERROR_CODE = "1002";

    @Override
    public boolean supports(final Class<?> clazz) {
        return clazz.isAssignableFrom(ReviewModDto.class);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        final ReviewModDto dto = (ReviewModDto) obj;

        VALIDATOR.validate(dto)
                .forEach(violation -> errors.rejectValue(violation.getPropertyPath().toString(), ERROR_CODE, violation.getMessage()));
    }
}
