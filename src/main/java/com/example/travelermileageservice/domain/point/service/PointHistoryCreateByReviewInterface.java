package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.review.entity.Review;

interface PointHistoryCreateByReviewInterface {

    void excute(final Review review);
}
