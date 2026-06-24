package com.skillstorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.models.Contribution;

public interface ContributionRepository extends JpaRepository<Contribution, Long>{

}
