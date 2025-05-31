package com.lifesimulator.lifesimulator.repositories;

import com.lifesimulator.lifesimulator.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
