package com.example.travelermileageservice.largetest;

import com.example.travelermileageservice.domain.point.repository.PointHistoryRepository;
import com.example.travelermileageservice.domain.point.service.PointGetService;
import com.example.travelermileageservice.domain.review.entity.Review;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import com.example.travelermileageservice.domain.review.service.ReviewAddService;
import com.example.travelermileageservice.domain.review.service.ReviewModService;
import com.example.travelermileageservice.domain.review.service.dto.ReviewAddDto;
import com.example.travelermileageservice.domain.review.service.dto.ReviewModDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest
class ReviewModLargeTest {

    @Autowired
    private ReviewAddService reviewAddService;

    @Autowired
    private ReviewModService reviewModService;

    @Autowired
    private PointGetService pointGetService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @DisplayName("리뷰 수정과 포인트 변경")
    @TestFactory
    Stream<DynamicTest> editReviewAndChangePoints() {
        final String content = "좋아요!";
        final UUID reviewId = UUID.randomUUID();
        final UUID user1 = UUID.randomUUID();
        final UUID placeId = UUID.randomUUID();
        final List<UUID> attachedPhotos = List.of(UUID.randomUUID(), UUID.randomUUID());

        return Stream.of(
                dynamicTest("내용(1) + 사진(1) + 최초(1) = 3점", () -> {
                    // given
                    final ReviewAddDto reviewAddDto = ReviewAddDto.of(reviewId, content, attachedPhotos, user1, placeId);

                    // when
                    reviewAddService.add(reviewAddDto);

                    // then
                    assertThat(reviewRepository.findById(reviewId)).isNotNull();
                    assertThat(pointGetService.get(user1)).isEqualTo(3);
                }),

                dynamicTest("기존(3) + 내용 변경(0) + 사진 1장 추가(0) = 3점", () -> {
                    // given
                    final String newContent = "변경된 좋아요!";
                    final List<UUID> newAttachedPhotos = new ArrayList<>(attachedPhotos);
                    newAttachedPhotos.add(UUID.randomUUID());
                    final ReviewModDto reviewModDto = ReviewModDto.of(reviewId, newContent, newAttachedPhotos);

                    // when
                    reviewModService.mod(reviewModDto);

                    // then
                    final Review review = reviewRepository.findById(reviewId).get();
                    assertThat(review.getContent()).isEqualTo(newContent);
                    assertThat(review.getAttachedPhotos()).hasSize(3);
                    assertThat(pointGetService.get(user1)).isEqualTo(3);
                }),

                dynamicTest("기존(3) + 사진 비우기(-1) = 2점", () -> {
                    // given
                    final String newContent = "변경된 좋아요!";
                    final List<UUID> newAttachedPhotos = List.of();
                    final ReviewModDto reviewModDto = ReviewModDto.of(reviewId, newContent, newAttachedPhotos);

                    // when
                    reviewModService.mod(reviewModDto);

                    // then
                    final Review review = reviewRepository.findById(reviewId).get();
                    assertThat(review.getContent()).isEqualTo(newContent);
                    assertThat(review.getAttachedPhotos()).isEmpty();

                    assertThat(pointGetService.get(user1)).isEqualTo(2);
                    assertThat(pointHistoryRepository.findTopByOrderByIdDesc().get().getPoint()).isEqualTo(-1);
                }),

                dynamicTest("기존(2) + 내용 비우기(-1) + 사진(1) = 2점", () -> {
                    // given
                    final String newContent = "";
                    final List<UUID> newAttachedPhotos = List.of(UUID.randomUUID());
                    final ReviewModDto reviewModDto = ReviewModDto.of(reviewId, newContent, newAttachedPhotos);

                    // when
                    reviewModService.mod(reviewModDto);

                    // then
                    final Review review = reviewRepository.findById(reviewId).get();
                    assertThat(review.getContent()).isEqualTo(newContent);
                    assertThat(review.getAttachedPhotos()).hasSize(1);

                    assertThat(pointGetService.get(user1)).isEqualTo(2);
                    assertThat(pointHistoryRepository.findTopByOrderByIdDesc().get().getPoint()).isZero();
                }),

                dynamicTest("기존(2) + 사진 비우기(-1) = 1점", () -> {
                    // given
                    final String newContent = "";
                    final List<UUID> newAttachedPhotos = List.of();
                    final ReviewModDto reviewModDto = ReviewModDto.of(reviewId, newContent, newAttachedPhotos);

                    // when
                    reviewModService.mod(reviewModDto);

                    // then
                    final Review review = reviewRepository.findById(reviewId).get();
                    assertThat(review.getContent()).isEqualTo(newContent);
                    assertThat(review.getAttachedPhotos()).isEmpty();

                    assertThat(pointGetService.get(user1)).isEqualTo(1);
                    assertThat(pointHistoryRepository.findTopByOrderByIdDesc().get().getPoint()).isEqualTo(-1);
                }),

                dynamicTest("기존(1) + 내용 채우기(1) + 사진 채우기(1) = 3점", () -> {
                    // given
                    final String newContent = "좋아요!!";
                    final List<UUID> newAttachedPhotos = List.of(UUID.randomUUID(), UUID.randomUUID());
                    final ReviewModDto reviewModDto = ReviewModDto.of(reviewId, newContent, newAttachedPhotos);

                    // when
                    reviewModService.mod(reviewModDto);

                    // then
                    final Review review = reviewRepository.findById(reviewId).get();
                    assertThat(review.getContent()).isEqualTo(newContent);
                    assertThat(review.getAttachedPhotos()).hasSize(2);

                    assertThat(pointGetService.get(user1)).isEqualTo(3);
                    assertThat(pointHistoryRepository.findTopByOrderByIdDesc().get().getPoint()).isEqualTo(2);
                })
        );
    }
}
