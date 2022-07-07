package com.example.travelermileageservice.domain.review.service;

import com.example.travelermileageservice.config.exception.BusinessException;
import com.example.travelermileageservice.config.exception.CustomValidationException;
import com.example.travelermileageservice.config.exception.ErrorCode;
import com.example.travelermileageservice.domain.point.service.PointCreateFacade;
import com.example.travelermileageservice.domain.review.entity.AttachedPhoto;
import com.example.travelermileageservice.domain.review.entity.Review;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import com.example.travelermileageservice.domain.review.service.dto.ReviewModDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.EventType.REVIEW_MOD;

@RequiredArgsConstructor
@Service
public class ReviewModService {

    private final PointCreateFacade pointCreateFacade;

    private final ReviewModValidator reviewModValidator;
    private final ReviewRepository reviewRepository;

    @Transactional
    public UUID mod(final ReviewModDto dto) {
        validate(dto);

        final Review review = reviewRepository.findById(dto.getReviewId()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        final List<AttachedPhoto> attachedPhotos = dto.getAttachedPhotoIds().stream()
                .map(AttachedPhoto::new)
                .collect(Collectors.toList());

        review.updateAttachedPhotos(attachedPhotos);
        review.updateContent(dto.getContent());

        pointCreateFacade.create(REVIEW_MOD, review.getId(), review.getCreatedBy());

        return review.getId();
    }

    private void validate(final ReviewModDto dto) {
        final BindingResult bindingResult = new BeanPropertyBindingResult(dto, ReviewModDto.class.getName());
        reviewModValidator.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult);
        }
    }
}
