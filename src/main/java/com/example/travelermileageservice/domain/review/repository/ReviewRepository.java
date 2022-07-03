package com.example.travelermileageservice.domain.review.repository;

import com.example.travelermileageservice.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByCreatedByAndPlaceId(UUID userId, UUID placeId);
}
