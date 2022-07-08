package com.example.travelermileageservice.domain.point.repository;

import com.example.travelermileageservice.domain.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, PointHistoryQuerydsl {

    /**
     * Large Test를 위한 기능
     *
     * @return 가장 마지막에 작성된 이력
     */
    Optional<PointHistory> findTopByOrderByIdDesc();
}
