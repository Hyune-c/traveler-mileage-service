package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import com.example.travelermileageservice.domain.point.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PointAddService {

    private final PointAddReviewAddService pointAddReviewAddService;
    private final PointAddReviewDeleteService pointAddReviewDeleteService;

    @Transactional
    public void add(final PointHistory.Type type, final UUID sourceId, final UUID userId) {
        switch (type) {
            case REVIEW_ADD:
                pointAddReviewAddService.reviewAdd(sourceId, userId);
                return;
            case REVIEW_DELETE:
                pointAddReviewDeleteService.reviewDelete(sourceId);
                return;
            default:
                throw new BusinessException("Unexpected value: " + type);
        }
    }
}
