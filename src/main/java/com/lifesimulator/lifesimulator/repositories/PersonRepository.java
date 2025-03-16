package com.lifesimulator.lifesimulator.repositories;

import com.lifesimulator.lifesimulator.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
