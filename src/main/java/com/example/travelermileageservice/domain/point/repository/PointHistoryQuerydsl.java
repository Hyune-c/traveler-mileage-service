package com.example.travelermileageservice.domain.point.repository;

import java.util.UUID;

interface PointHistoryQuerydsl {

    Integer getPoint(final UUID userId);
}
