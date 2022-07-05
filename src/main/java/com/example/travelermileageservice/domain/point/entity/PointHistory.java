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
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public final class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID sourceId;

    @Column(updatable = false, nullable = false)
    private Integer point;

    @Column(updatable = false, nullable = false)
    private Integer bonusPoint;

    private PointHistory(final Type type, final UUID sourceId, final UUID userId, final Integer point, final Integer bonusPoint) {
        this.type = type;
        this.sourceId = sourceId;
        this.point = point;
        this.bonusPoint = bonusPoint;

        this.createdBy = userId;
        this.updatedBy = userId;
    }

    public static PointHistory of(final Type type, final UUID sourceId, final UUID userId, final Integer point, final Integer bonusPoint) {
        return new PointHistory(type, sourceId, userId, point, bonusPoint);
    }

    public enum Type {
        REVIEW_ADD, REVIEW_MOD, REVIEW_DELETE;
    }
}
