package com.lifesimulator.lifesimulator.repositories;

import com.lifesimulator.lifesimulator.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
