package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import com.example.travelermileageservice.domain.point.entity.PointHistory;
import com.example.travelermileageservice.domain.review.entity.Review;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 사용자의 관심사는 오직 `이벤트에 맞는 포인트 적립` 뿐입니다.<br>
 * 따라서 그 과정을 캡슐화 합니다.
 */
@RequiredArgsConstructor
@Service
public class PointCreateFacade {

    /**
     * facade 패턴의 활용으로 내부 로직 교체가 용이합니다. (ex. 외부 통신) <br>
     * - 지금의 구현에서는 PointHistory를 통해 이루어 집니다.
     */
    private final PointHistoryCreateByReviewAddImpl pointHistoryCreateByReviewAdd;
    private final PointHistoryCreateByReviewModmpl pointHistoryCreateByReviewMod;
    private final PointHistoryCreateByReviewDeleteImpl pointHistoryCreateByReviewDelete;

    private final ReviewRepository reviewRepository;

    @Transactional
    public void create(final PointHistory.EventType eventType, final UUID sourceId, final UUID userId) {
        if (eventType.isReview()) {
            byReview(eventType, sourceId, userId);
            return;
        }

        throw new BusinessException("Unexpected value: " + eventType);
    }

    private void byReview(final PointHistory.EventType eventType, final UUID reviewId, final UUID userId) {
        final Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("찾을 수 없는 리뷰"));

        switch (eventType) {
            case REVIEW_ADD:
                pointHistoryCreateByReviewAdd.excute(review);
                return;
            case REVIEW_MOD:
                pointHistoryCreateByReviewMod.excute(review);
                return;
            case REVIEW_DELETE:
                pointHistoryCreateByReviewDelete.excute(review);
                return;
            default:
                throw new BusinessException("Unexpected value: " + eventType);
        }
    }
}
