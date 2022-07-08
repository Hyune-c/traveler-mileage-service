package com.example.travelermileageservice.domain.review.repository;

import java.util.UUID;

interface ReviewQuerydsl {

    Boolean isFirstReviewAtPlace(final UUID reviewId);
}
