package com.lifesimulator.lifesimulator.repositories;

import com.lifesimulator.lifesimulator.models.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByPlayerId(Long playerId);
}
