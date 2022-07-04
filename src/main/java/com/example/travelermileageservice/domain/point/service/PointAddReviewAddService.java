
package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import com.example.travelermileageservice.domain.point.entity.PointHistory;
import com.example.travelermileageservice.domain.point.repository.PointHistoryRepository;
import com.example.travelermileageservice.domain.review.entity.AttachedPhoto;
import com.example.travelermileageservice.domain.review.entity.Review;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;
import java.util.function.ToIntFunction;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_ADD;

@RequiredArgsConstructor
@Service
public class PointAddReviewAddService {

    private final PointHistoryRepository pointHistoryRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void reviewAdd(final UUID reviewId, final UUID userId) {
        final Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessException("찾을 수 없는 리뷰"));
        final int point = calculatePoint(review);
        final int bonusPoint = calculateBonusPoint(reviewId);

        pointHistoryRepository.save(PointHistory.of(REVIEW_ADD, reviewId, userId, point, bonusPoint));
    }

    private static int calculatePoint(final Review review) {
        final ToIntFunction<String> contentPointFunction = content -> content.isEmpty() ? 0 : 1;
        final ToIntFunction<Collection<AttachedPhoto>> attachedphotoPointFunction = attachedPhotos -> attachedPhotos.isEmpty() ? 0 : 1;

        return contentPointFunction.applyAsInt(review.getContent()) + attachedphotoPointFunction.applyAsInt(review.getAttachedPhotos());
    }

    private int calculateBonusPoint(final UUID reviewId) {
        return reviewRepository.isFirstReviewAtPlace(reviewId) ? 1 : 0;
    }
}
