package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.point.entity.PointHistory;
import com.example.travelermileageservice.domain.point.repository.PointHistoryRepository;
import com.example.travelermileageservice.domain.review.entity.AttachedPhoto;
import com.example.travelermileageservice.domain.review.entity.Review;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;
import java.util.function.ToIntFunction;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.EventType.REVIEW_ADD;

@RequiredArgsConstructor
@Component
class PointHistoryCreateByReviewAddImpl implements PointHistoryCreateByReviewInterface {

    private final PointHistoryRepository pointHistoryRepository;
    private final ReviewRepository reviewRepository;

    /**
     * point - 내용 1점, 사진 1점<br>
     * bonusPoint - 최초 작성 1점
     *
     * @param review 작성한 리뷰
     */
    @Transactional
    @Override
    public void excute(final Review review) {
        final int point = calculatePoint(review);
        final int bonusPoint = calculateBonusPoint(review.getId());

        pointHistoryRepository.save(PointHistory.of(REVIEW_ADD, review.getId(), review.getCreatedBy(), point, bonusPoint));
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
