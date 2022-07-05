package com.example.travelermileageservice.domain.review.service;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import com.example.travelermileageservice.domain.point.service.PointCreateFacade;
import com.example.travelermileageservice.domain.review.entity.Review;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_DELETE;

@RequiredArgsConstructor
@Service
public class ReviewDeleteService {

    private final PointCreateFacade pointCreateFacade;

    private final ReviewRepository reviewRepository;

    @Transactional
    public void delete(final UUID reviewId) {
        final Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessException("찾을 수 없는 리뷰"));
        review.delete();
        pointCreateFacade.create(REVIEW_DELETE, review.getId(), review.getCreatedBy());
    }
}
