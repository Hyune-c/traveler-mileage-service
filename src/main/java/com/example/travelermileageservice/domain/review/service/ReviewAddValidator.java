package com.example.travelermileageservice.domain.review.service;

import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import com.example.travelermileageservice.domain.review.service.dto.ReviewAddDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.Validation;

@Slf4j
@RequiredArgsConstructor
@Component
class ReviewAddValidator implements Validator {

    private static final javax.validation.Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private static final String ERROR_CODE = "1001";
    private static final String ERROR_MESSAGE = "작성한 리뷰가 존재합니다.";

    private final ReviewRepository reviewRepository;

    @Override
    public boolean supports(final Class<?> clazz) {
        return clazz.isAssignableFrom(ReviewAddDto.class);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        final ReviewAddDto dto = (ReviewAddDto) obj;

        // 사용자는 장소당 1개의 리뷰만 작성할 수 있습니다.
        if (reviewRepository.existsByCreatedByAndPlaceIdAndDeleted(dto.getUserId(), dto.getPlaceId(), false)) {
            errors.rejectValue("placeId", ERROR_CODE, ERROR_MESSAGE);
        }

        VALIDATOR.validate(dto)
                .forEach(violation -> errors.rejectValue(violation.getPropertyPath().toString(), ERROR_CODE, violation.getMessage()));
    }
}
