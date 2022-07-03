package com.example.travelermileageservice.domain.review.service;

import com.example.travelermileageservice.domain.review.entity.AttachedPhoto;
import com.example.travelermileageservice.domain.review.entity.Review;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import com.example.travelermileageservice.domain.review.service.dto.ReviewAddDto;
import com.example.travelermileageservice.domain.review.service.exception.ReviewAddException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ReviewAddServiceTest {

    @Autowired
    private ReviewAddService reviewAddService;

    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void afterEach() {
        reviewRepository.deleteAll();
    }

    @DisplayName("성공")
    @Nested
    class Success {

        @DisplayName("해당 장소에 삭제된 자신의 리뷰가 존재")
        @Test
        void yourReviewHasBeenDeletedInThatPlace() {
            // given
            final String content = "좋아요!";
            final UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
            final UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
            final UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
            final List<AttachedPhoto> attachedPhotos = List.of(
                    new AttachedPhoto(UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8")),
                    new AttachedPhoto(UUID.fromString("afb0cef2-851d-4a50-bb07-9cc15cbdc332")));

            final Review review = new Review(reviewId, userId, content, placeId, attachedPhotos);
            review.delete();
            reviewRepository.save(review);

            final ReviewAddDto reviewAddDto = ReviewAddDto.of(UUID.randomUUID(), content, List.of(UUID.randomUUID(), UUID.randomUUID()), userId, placeId);

            // when
            reviewAddService.add(reviewAddDto);

            // then
            assertThat(reviewRepository.findAllByCreatedBy(userId)).hasSize(2);
        }
    }

    @DisplayName("실패")
    @Nested
    class Failed {

        @DisplayName("해당 장소에 이미 자신의 리뷰가 존재")
        @Test
        void yourReviewAlreadyExistsOnThatPlace() {
            // given
            final String content = "좋아요!";
            final UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
            final UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
            final UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
            final List<AttachedPhoto> attachedPhotos = List.of(
                    new AttachedPhoto(UUID.fromString("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8")),
                    new AttachedPhoto(UUID.fromString("afb0cef2-851d-4a50-bb07-9cc15cbdc332")));

            final Review review = new Review(reviewId, userId, content, placeId, attachedPhotos);
            reviewRepository.save(review);

            final ReviewAddDto reviewAddDto = ReviewAddDto.of(UUID.randomUUID(), content, List.of(UUID.randomUUID(), UUID.randomUUID()), userId, placeId);

            // when
            assertThatThrownBy(() -> reviewAddService.add(reviewAddDto))
                    .isInstanceOf(ReviewAddException.class);

            // then
            assertThat(reviewRepository.findAllByCreatedBy(userId)).hasSize(1);
        }
    }
}
