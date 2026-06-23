package com.skillstorm.models;

import com.skillstorm.enums.ContributionCategory;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Persistent junction entity that models a single contribution event.
 */
@Entity
@Table(name = "contributions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contribution {

    /**
     * Surrogate primary key, auto-incremented by the database sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;


    /**
     * The user who logged this contribution.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_contributions_user_id")
    )
    private User user;

    /**
     * The retirement goal that this contribution is working toward.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "goal_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_contributions_goal_id")
    )
    private RetirementGoal goal;

    /**
     * The funding source from which this contribution was made.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "funding_source_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_contributions_funding_source_id")
    )
    private FundingSource fundingSource;


    /**
     * Dollar amount of this contribution.
     * Stored with up to 15 digits and 2 decimal places to avoid floating-point drift.
     */
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * Calendar date on which the contribution was made.
     */
    @Column(name = "contribution_date", nullable = false)
    private LocalDate contributionDate;

    /**
     * Contribution category, stored as its enum name string for readability.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ContributionCategory category;


}