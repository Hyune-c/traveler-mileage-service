package com.example.travelermileageservice.domain.review.service;

import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import com.example.travelermileageservice.domain.review.service.dto.ReviewAddDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReviewAddValidator implements Validator {

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

        if (reviewRepository.existsByCreatedByAndPlaceId(dto.getUserId(), dto.getPlaceId())) {
            log.error(ERROR_MESSAGE);
            errors.reject(ERROR_CODE, ERROR_MESSAGE);
        }
    }
}
