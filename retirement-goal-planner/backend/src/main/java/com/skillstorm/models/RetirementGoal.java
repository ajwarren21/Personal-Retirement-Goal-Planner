package com.skillstorm.models;

import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistent entity representing a single retirement savings goal owned by a User.
 */
@Entity
@Table(name = "retirement_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetirementGoal {

    /**
     * Primary key, auto-incremented by the database sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * The user who created and owns this goal.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_goals_user_id")
    )

    private User user;


    /**
     * Human-readable name for this goal
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The age at which the user intends to retire under this goal.
     */
    @Column(name = "target_retirement_age", nullable = false)
    private Integer targetRetirementAge;

    /**
     * The total portfolio value the user aims to accumulate.
     * Must be greater than zero. Stored with up to 15 digits and 2 decimal places
     */
    @Column(name = "target_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal targetAmount;

    /**
     * All contributions that have been logged toward this goal.
     */
    @OneToMany(
        mappedBy = "goal",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<Contribution> contributions = new ArrayList<>();

    /**
     * RetirementGoal Notes.
     */
    @Column(columnDefinition = "TEXT")
    private String notes;

}