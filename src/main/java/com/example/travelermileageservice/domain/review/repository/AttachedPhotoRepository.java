package com.example.travelermileageservice.domain.review.repository;

import com.example.travelermileageservice.domain.review.entity.AttachedPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttachedPhotoRepository extends JpaRepository<AttachedPhoto, Long> {

    Optional<AttachedPhoto> findByPhotoId(UUID photoId);
}
