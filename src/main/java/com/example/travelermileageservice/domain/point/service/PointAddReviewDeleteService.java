package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import com.example.travelermileageservice.domain.point.entity.PointHistory;
import com.example.travelermileageservice.domain.point.repository.PointHistoryRepository;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_ADD;
import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_DELETE;
import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_MOD;

@RequiredArgsConstructor
@Service
public class PointAddReviewDeleteService {

    private final ReviewRepository reviewRepository;
    private final PointHistoryRepository pointHistoryRepository;

    /**
     * 리뷰 삭제시 포인트 차감 이력을 기록합니다.
     *
     * @param reviewId 삭제하려는 리뷰 id
     */
    @Transactional
    public void reviewDelete(final UUID reviewId) {
        final UUID userId = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("찾을 수 없는 리뷰"))
                .getCreatedBy();
        final Integer point = pointHistoryRepository.getPoint(Set.of(REVIEW_ADD, REVIEW_MOD), reviewId);

        pointHistoryRepository.save(PointHistory.of(REVIEW_DELETE, reviewId, userId, point * -1, 0));
    }
}
