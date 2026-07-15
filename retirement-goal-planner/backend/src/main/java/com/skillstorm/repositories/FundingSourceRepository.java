package com.skillstorm.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.models.FundingSource;

@Repository
public interface FundingSourceRepository extends JpaRepository<FundingSource, Long> {

    // Standard get all funding sources for a specific user
    List<FundingSource> findByUserId(Long userId);

    // Get a funding source by ID and user ID to ensure the funding source belongs to the user
    Optional<FundingSource> findByIdAndUserId(Long id, Long userId);

}
