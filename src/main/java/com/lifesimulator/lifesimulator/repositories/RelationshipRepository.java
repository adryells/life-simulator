package com.lifesimulator.lifesimulator.repositories;

import com.lifesimulator.lifesimulator.models.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
}
