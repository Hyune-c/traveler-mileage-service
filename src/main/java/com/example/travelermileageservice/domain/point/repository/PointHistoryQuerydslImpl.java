package com.example.travelermileageservice.domain.point.repository;

import com.example.travelermileageservice.domain.point.entity.PointHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.UUID;

import static com.example.travelermileageservice.domain.point.entity.QPointHistory.pointHistory;

@RequiredArgsConstructor
class PointHistoryQuerydslImpl implements PointHistoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public Integer getPoint(final UUID userId) {
        return queryFactory
                .select(pointHistory.point.sum().add(pointHistory.bonusPoint.sum()).coalesce(0))
                .from(pointHistory)
                .where(pointHistory.createdBy.eq(userId))
                .fetchOne();
    }

    @Override
    public Integer getPoint(final Set<PointHistory.Type> types, final UUID sourceId) {
        return queryFactory
                .select(pointHistory.point.sum().add(pointHistory.bonusPoint.sum()).coalesce(0))
                .from(pointHistory)
                .where(pointHistory.type.in(types)
                        .and(pointHistory.sourceId.eq(sourceId)))
                .fetchOne();
    }
}
