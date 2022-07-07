package com.example.travelermileageservice.domain.point.entity;

import com.example.travelermileageservice.domain.base.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public final class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID eventId;

    @Column(updatable = false, nullable = false)
    private Integer point;

    @Column(updatable = false, nullable = false)
    private Integer bonusPoint;

    private PointHistory(final EventType eventType, final UUID eventId, final UUID userId, final Integer point, final Integer bonusPoint) {
        this.eventType = eventType;
        this.eventId = eventId;
        this.point = point;
        this.bonusPoint = bonusPoint;

        this.createdBy = userId;
        this.updatedBy = userId;
    }

    public static PointHistory of(final EventType eventType, final UUID sourceId, final UUID userId, final Integer point, final Integer bonusPoint) {
        return new PointHistory(eventType, sourceId, userId, point, bonusPoint);
    }

    /**
     * 포인트 이력이 쌓이는 이벤트 종류
     */
    public enum EventType {
        REVIEW_ADD, REVIEW_MOD, REVIEW_DELETE;

        public boolean isReview() {
            return List.of(REVIEW_ADD, REVIEW_MOD, REVIEW_DELETE).contains(this);
        }
    }
}
