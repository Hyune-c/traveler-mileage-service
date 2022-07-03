package com.example.travelermileageservice.domain.review.service;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import com.example.travelermileageservice.domain.review.entity.AttachedPhoto;
import com.example.travelermileageservice.domain.review.entity.Review;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import com.example.travelermileageservice.domain.review.service.dto.ReviewModDto;
import com.example.travelermileageservice.domain.review.service.exception.ReviewAddException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewModService {

    private final ReviewModValidator reviewModValidator;
    private final ReviewRepository reviewRepository;

    @Transactional
    public UUID mod(final ReviewModDto dto) {
        validate(dto);

        final Review review = reviewRepository.findById(dto.getReviewId()).orElseThrow(() -> new BusinessException("찾을 수 없는 리뷰"));
        final List<AttachedPhoto> attachedPhotos = dto.getAttachedPhotoIds().stream()
                .map(AttachedPhoto::new)
                .collect(Collectors.toList());

        review.updateAttachedPhotos(attachedPhotos);
        review.updateContent(dto.getContent());

        return review.getId();
    }

    private void validate(final ReviewModDto dto) {
        final Errors errors = new BeanPropertyBindingResult(dto, ReviewModDto.class.getName());
        reviewModValidator.validate(dto, errors);

        if (errors.hasErrors()) {
            throw new ReviewAddException(errors);
        }
    }
}
