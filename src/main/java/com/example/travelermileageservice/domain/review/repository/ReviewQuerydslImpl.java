package com.example.travelermileageservice.domain.review.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static com.example.travelermileageservice.domain.review.entity.QReview.review;

@RequiredArgsConstructor
class ReviewQuerydslImpl implements ReviewQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean isFirstReviewAtPlace(final UUID reviewId) {
        return queryFactory
                .select(review.id)
                .from(review)
                .where(review.placeId.eq(JPAExpressions.select(review.placeId)
                                .from(review)
                                .where(review.id.eq(reviewId)))
                        .and(review.id.ne(reviewId))
                        .and(review.deleted.isFalse()))
                .fetchFirst() == null;
    }
}
