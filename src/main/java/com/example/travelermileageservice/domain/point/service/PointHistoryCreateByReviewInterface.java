package com.example.travelermileageservice.domain.point.service;

import com.example.travelermileageservice.domain.review.entity.Review;

interface PointHistoryCreateByReviewInterface {

    /**
     * 만들어진 review 기반으로 포인트 이력을 생성합니다.
     * @param review 만들어진 리뷰
     */
    void excute(final Review review);
}
