package com.skillstorm.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.models.Contribution;

public interface ContributionRepository extends JpaRepository<Contribution, Long>{
    // Standard get all funding sources for a specific user
    List<Contribution> findByUserId(Long userId);

    // Get a funding source by ID and user ID to ensure the funding source belongs to the user
    Optional<Contribution> findByIdAndUserId(Long id, Long userId);
}
