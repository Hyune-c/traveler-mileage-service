package com.example.travelermileageservice.domain.point.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PointGetServiceTest {

    @Autowired
    private PointGetService pointGetService;

    @DisplayName("이력이 존재하지 않으면 0점")
    @Test
    void _0PointsIfNoHistoryExists() {
        // given
        final UUID userId = UUID.randomUUID();

        // when
        final Integer point = pointGetService.get(userId);

        // then
        assertThat(point).isZero();
    }
}
