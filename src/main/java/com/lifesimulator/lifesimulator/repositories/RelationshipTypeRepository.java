package com.lifesimulator.lifesimulator.repositories;

import com.lifesimulator.lifesimulator.models.RelationshipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipTypeRepository  extends JpaRepository<RelationshipType, Long> {
    RelationshipType findByName(String name);
}
