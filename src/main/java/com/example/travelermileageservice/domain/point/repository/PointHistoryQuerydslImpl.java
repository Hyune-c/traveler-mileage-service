package com.example.travelermileageservice.domain.point.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static com.example.travelermileageservice.domain.point.entity.QPointHistory.pointHistory;

@RequiredArgsConstructor
class PointHistoryQuerydslImpl implements PointHistoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public Integer getPoint(final UUID userId) {
        return queryFactory
                .select(pointHistory.addPoint.sum().add(pointHistory.addBonusPoint.sum()).coalesce(0))
                .from(pointHistory)
                .where(pointHistory.createdBy.eq(userId))
                .fetchOne();
    }
}
