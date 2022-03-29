package com.gmail.bsbgroup6.repository.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "applications_conversion")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "unique_number", unique = true, nullable = false)
    private UUID uniqueNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "value_legal")
    private Integer valueLegal;

    @Column(name = "value_individual")
    private Integer valueIndividual;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "percentage_conversion")
    private Float conversionPercentage;

    @Column(name = "legal_id")
    private Long legalEntityId;

    @OneToOne(
            fetch = FetchType.LAZY,
            mappedBy = "application",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private ApplicationDetails applicationDetails;
}
