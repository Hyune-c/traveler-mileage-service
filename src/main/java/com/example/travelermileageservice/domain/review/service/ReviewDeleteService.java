package com.example.travelermileageservice.domain.review.service;

import com.example.travelermileageservice.config.exception.BusinessException;
import com.example.travelermileageservice.config.exception.ErrorCode;
import com.example.travelermileageservice.domain.point.service.PointCreateFacade;
import com.example.travelermileageservice.domain.review.entity.Review;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.EventType.REVIEW_DELETE;

@RequiredArgsConstructor
@Service
public class ReviewDeleteService {

    private final PointCreateFacade pointCreateFacade;

    private final ReviewRepository reviewRepository;

    @Transactional
    public void delete(final UUID reviewId) {
        final Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        review.delete();
        pointCreateFacade.create(REVIEW_DELETE, review.getId(), review.getCreatedBy());
    }
}
