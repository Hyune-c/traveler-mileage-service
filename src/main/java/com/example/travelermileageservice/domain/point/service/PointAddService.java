package com.example.travelermileageservice.domain.point.service;

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
                return;
            default:
        }
    }

    private void reviewAdd(final UUID reviewId, final UUID userId) {
        final int addBonusPoint = reviewRepository.isFirstReviewAtPlace(reviewId) ? 1 : 0;
        pointHistoryRepository.save(PointHistory.of(REVIEW_ADD, reviewId, userId, addBonusPoint));
    }
}
