package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.point.entity.PointHistory;
import com.example.travelermileageservice.domain.point.repository.PointHistoryRepository;
import com.example.travelermileageservice.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_ADD;
import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_DELETE;
import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_MOD;

@RequiredArgsConstructor
@Component
class PointHistoryCreateByReviewDeleteImpl implements PointHistoryCreateByReviewInterface {

    private final PointHistoryRepository pointHistoryRepository;

    /**
     * 해당 리뷰에 연관된 모든 포인트 기록을 원상 복구시키는 이력을 기록합니다.
     *
     * @param review 삭제한 리뷰
     */
    @Transactional
    @Override
    public void excute(final Review review) {
        final Integer point = pointHistoryRepository.getPoint(Set.of(REVIEW_ADD, REVIEW_MOD), review.getId());

        pointHistoryRepository.save(PointHistory.of(REVIEW_DELETE, review.getId(), review.getCreatedBy(), point * -1, 0));
    }
}
