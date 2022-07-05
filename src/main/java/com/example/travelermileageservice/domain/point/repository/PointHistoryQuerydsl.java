package com.example.travelermileageservice.domain.point.repository;

import com.example.travelermileageservice.domain.point.entity.PointHistory;

import java.util.Set;
import java.util.UUID;

interface PointHistoryQuerydsl {

    /**
     * @param userId 회원 id
     * @return 회원의 현재 포인트
     */
    Integer getPoint(final UUID userId);

    /**
     * @param types    이벤트 구분
     * @param sourceId 이벤트 id
     * @return 이벤트 구분에 맞는 이벤트 id로 적립된 포인트
     */
    Integer getPoint(final Set<PointHistory.Type> types, final UUID sourceId);

    /**
     * @param types     이벤트 구분
     * @param sourceId  이벤트 id
     * @param pointOnly 포인트만 조회할지 여부 (보너스 포인트 배제)
     * @return 이벤트 구분에 맞는 이벤트 id로 적립된 포인트
     */
    Integer getPoint(final Set<PointHistory.Type> types, final UUID sourceId, final Boolean pointOnly);
}
