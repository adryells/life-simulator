package com.lifesimulator.lifesimulator.repositories;

import com.lifesimulator.lifesimulator.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>{
}
