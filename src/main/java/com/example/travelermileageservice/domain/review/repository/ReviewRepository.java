package com.example.travelermileageservice.domain.review.repository;

import com.example.travelermileageservice.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID>, ReviewQuerydsl {

    boolean existsByCreatedByAndPlaceIdAndDeleted(UUID userId, UUID placeId, Boolean deleted);

    Optional<Review> findById(UUID reviewId);
}
