package com.example.travelermileageservice.domain.point.repository;

import com.example.travelermileageservice.config.JpaConfig;
import com.example.travelermileageservice.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class PointHistoryRepositoryTest {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @DisplayName("이력이 존재하지 않으면 0점")
    @Test
    void _0PointsIfNoHistoryExists() {
        // given
        final UUID userId = UUID.randomUUID();

        // when
        final Integer point = pointHistoryRepository.getPoint(userId);

        // then
        assertThat(point).isZero();
    }
}
