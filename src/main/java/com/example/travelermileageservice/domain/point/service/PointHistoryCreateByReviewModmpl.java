package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.point.entity.PointHistory;
import com.example.travelermileageservice.domain.point.repository.PointHistoryRepository;
import com.example.travelermileageservice.domain.review.entity.AttachedPhoto;
import com.example.travelermileageservice.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.function.ToIntFunction;

import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_ADD;
import static com.example.travelermileageservice.domain.point.entity.PointHistory.Type.REVIEW_MOD;

@RequiredArgsConstructor
@Component
class PointHistoryCreateByReviewModmpl implements PointHistoryCreateByReviewInterface {

    private final PointHistoryRepository pointHistoryRepository;

    /**
     * `수정된 리뷰의 포인트`에서 `이전의 리뷰 포인트 합산`을 빼면 수정으로 인한 포인트 변화를 기록할 수 있습니다.<br>
     * - 수정으로 인한 `최초 작성 보너스 포인트 적립`은 고려하지 않습니다.
     *
     * @param review 수정한 리뷰
     */
    @Transactional
    @Override
    public void excute(final Review review) {
        final Integer previousPoint = pointHistoryRepository.getPoint(Set.of(REVIEW_ADD, REVIEW_MOD), review.getId(), true);
        final Integer currentPoint = calculatePoint(review);

        pointHistoryRepository.save(PointHistory.of(REVIEW_MOD, review.getId(), review.getCreatedBy(), currentPoint - previousPoint, 0));
    }

    private static int calculatePoint(final Review review) {
        final ToIntFunction<String> contentPointFunction = content -> content.isEmpty() ? 0 : 1;
        final ToIntFunction<Collection<AttachedPhoto>> attachedphotoPointFunction = attachedPhotos -> attachedPhotos.isEmpty() ? 0 : 1;

        return contentPointFunction.applyAsInt(review.getContent()) + attachedphotoPointFunction.applyAsInt(review.getAttachedPhotos());
    }
}
