package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.point.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PointGetService {

    private final PointHistoryRepository pointHistoryRepository;

    /**
     * @param userId 포인트를 가져오려는 회원
     * @return 현재 포인트
     */
    @Transactional(readOnly = true)
    public Integer get(final UUID userId) {
        return pointHistoryRepository.getPoint(userId);
    }
}
