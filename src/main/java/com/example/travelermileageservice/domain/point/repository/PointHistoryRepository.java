package com.example.travelermileageservice.domain.point.repository;

import com.example.travelermileageservice.domain.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, PointHistoryQuerydsl {

    Optional<PointHistory> findByTypeAndSourceId(PointHistory.Type type, UUID sourceId);
}
