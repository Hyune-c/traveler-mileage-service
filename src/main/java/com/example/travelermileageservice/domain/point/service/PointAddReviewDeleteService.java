package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import com.example.travelermileageservice.domain.point.entity.PointHistory;
import com.example.travelermileageservice.domain.point.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_ADD;

@RequiredArgsConstructor
@Service
public class PointAddReviewDeleteService {

    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public void reviewDelete(final UUID reviewId) {
        final PointHistory reviewAddPointHistory = pointHistoryRepository.findByTypeAndSourceId(REVIEW_ADD, reviewId)
                .orElseThrow(() -> new BusinessException("찾을 수 없는 리뷰"));
        pointHistoryRepository.save(reviewAddPointHistory.convert());
    }
}
