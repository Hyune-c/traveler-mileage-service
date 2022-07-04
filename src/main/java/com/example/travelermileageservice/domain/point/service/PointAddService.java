package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import com.example.travelermileageservice.domain.point.entity.PointHistory;
import com.example.travelermileageservice.domain.point.repository.PointHistoryRepository;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_ADD;

@RequiredArgsConstructor
@Service
public class PointAddService {

    private final PointHistoryRepository pointHistoryRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void add(final PointHistory.Type type, final UUID sourceId, final UUID userId) {
        switch (type) {
            case REVIEW_ADD:
                reviewAdd(sourceId, userId);
                return;
            case REVIEW_DELETE:
                reviewDelete(sourceId);
                return;
            default:
                throw new BusinessException("Unexpected value: " + type);
        }
    }

    private void reviewAdd(final UUID reviewId, final UUID userId) {
        final int addBonusPoint = reviewRepository.isFirstReviewAtPlace(reviewId) ? 1 : 0;
        pointHistoryRepository.save(PointHistory.of(REVIEW_ADD, reviewId, userId, addBonusPoint));
    }

    private void reviewDelete(final UUID reviewId) {
        final PointHistory reviewAddPointHistory = pointHistoryRepository.findByTypeAndSourceId(REVIEW_ADD, reviewId)
                .orElseThrow(() -> new BusinessException("찾을 수 없는 리뷰"));
        pointHistoryRepository.save(reviewAddPointHistory.convert());
    }
}
