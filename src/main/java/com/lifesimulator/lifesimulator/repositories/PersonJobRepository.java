package com.lifesimulator.lifesimulator.repositories;

import com.lifesimulator.lifesimulator.models.PersonJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonJobRepository extends JpaRepository<PersonJob, Long> {
    List<PersonJob> findByPersonId(Long personId);
}
