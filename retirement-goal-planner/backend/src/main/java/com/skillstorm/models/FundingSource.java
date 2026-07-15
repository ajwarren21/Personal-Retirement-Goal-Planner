package com.skillstorm.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skillstorm.enums.SourceType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Table(name = "funding_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingSource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    @NotBlank
    @Column(nullable = false)
    private String institution;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "fundingSource", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contribution> contributions;

}
