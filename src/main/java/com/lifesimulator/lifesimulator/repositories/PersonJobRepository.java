package com.lifesimulator.lifesimulator.repositories;

import com.lifesimulator.lifesimulator.models.PersonJob;
import com.lifesimulator.lifesimulator.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonJobRepository extends JpaRepository<PersonJob, Long> {
    Optional<PersonJob> findFirstByPlayerAndEndDateIsNullOrderByStartDateDesc(Player player);
}
