package com.example.travelermileageservice.domain.review;

import com.example.travelermileageservice.domain.point.service.PointGetService;
import com.example.travelermileageservice.domain.review.entity.AttachedPhoto;
import com.example.travelermileageservice.domain.review.repository.ReviewRepository;
import com.example.travelermileageservice.domain.review.service.ReviewAddService;
import com.example.travelermileageservice.domain.review.service.ReviewDeleteService;
import com.example.travelermileageservice.domain.review.service.dto.ReviewAddDto;
import com.example.travelermileageservice.domain.review.service.exception.ReviewAddException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest
class ReviewServiceLargeTest {

    @Autowired
    private ReviewAddService reviewAddService;

    @Autowired
    private ReviewDeleteService reviewDeleteService;

    @Autowired
    private PointGetService pointGetService;

    @Autowired
    private ReviewRepository reviewRepository;

    @DisplayName("리뷰 작성")
    @TestFactory
    Stream<DynamicTest> dynamicTestsFromCollection() {
        final String content = "좋아요!";
        final UUID reviewId = UUID.randomUUID();
        final UUID user1 = UUID.randomUUID();
        final UUID user2 = UUID.randomUUID();
        final UUID placeId = UUID.randomUUID();
        final List<AttachedPhoto> attachedPhotos = List.of(
                new AttachedPhoto(UUID.randomUUID()),
                new AttachedPhoto(UUID.randomUUID()));

        return Stream.of(
                dynamicTest("user1이 리뷰 작성", () -> {
                    // given
                    final ReviewAddDto reviewAddDto = ReviewAddDto.of(reviewId, content, List.of(UUID.randomUUID(), UUID.randomUUID()), user1, placeId);

                    // when
                    reviewAddService.add(reviewAddDto);

                    // then
                    assertThat(reviewRepository.findById(reviewId)).isNotNull();
                    assertThat(pointGetService.get(user1)).isEqualTo(2);
                }),

                dynamicTest("user1의 리뷰가 존재하는 장소에 user1이 리뷰 작성", () -> {
                    // given
                    final ReviewAddDto reviewAddDto = ReviewAddDto.of(UUID.randomUUID(), content, List.of(UUID.randomUUID(), UUID.randomUUID()), user1, placeId);

                    // when
                    assertThatThrownBy(() -> reviewAddService.add(reviewAddDto))
                            .isInstanceOf(ReviewAddException.class);

                    // then

                }),

                dynamicTest("user1이 작성한 리뷰 삭제", () -> {
                    // given

                    // when
                    reviewDeleteService.delete(reviewId);

                    // then
                    assertThat(reviewRepository.findById(reviewId)).isPresent();
                    assertThat(reviewRepository.findById(reviewId).get().getDeleted()).isTrue();
                    // TODO 리뷰 삭제 기능 구현 후 포인트 차감 검증 추가
                }),

                dynamicTest("user1이 삭제된 리뷰가 있는 장소에 리뷰 작성", () -> {
                    // given
                    final ReviewAddDto reviewAddDto = ReviewAddDto.of(UUID.randomUUID(), content, List.of(UUID.randomUUID(), UUID.randomUUID()), user1, placeId);

                    // when
                    reviewAddService.add(reviewAddDto);

                    // then
                    assertThat(reviewRepository.findById(reviewId)).isNotNull();
                    // TODO 리뷰 삭제 기능 구현 후 수정
                    assertThat(pointGetService.get(user1)).isEqualTo(4);
                }),

                dynamicTest("user1이 리뷰를 작성한 장소에 user2가 작성 - 포인트 1점", () -> {
                    // given
                    final ReviewAddDto reviewAddDto = ReviewAddDto.of(UUID.randomUUID(), content, List.of(UUID.randomUUID(), UUID.randomUUID()), user2, placeId);

                    // when
                    reviewAddService.add(reviewAddDto);

                    // then
                    assertThat(pointGetService.get(user2)).isEqualTo(1);
                }),

                dynamicTest("user2가 새로운 장소에 리뷰를 작성 - 포인트 1 + 2점", () -> {
                    // given
                    final ReviewAddDto reviewAddDto = ReviewAddDto.of(UUID.randomUUID(), content, List.of(UUID.randomUUID(), UUID.randomUUID()), user2, UUID.randomUUID());

                    // when
                    reviewAddService.add(reviewAddDto);

                    // then
                    assertThat(pointGetService.get(user2)).isEqualTo(3);
                })
        );
    }
}
