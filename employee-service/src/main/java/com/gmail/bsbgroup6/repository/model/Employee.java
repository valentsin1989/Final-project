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

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "full_name", unique = true, nullable = false)
    private String fullName;

    @Column(name = "recruitment_date")
    private String recruitmentDate;

    @Column(name = "termination_date")
    private String terminationDate;

    @Column(name = "person_iban_byn", unique = true, nullable = false)
    private String personIbanByn;

    @Column(name = "person_iban_currency", unique = true, nullable = false)
    private String personIbanCurrency;

    @Column(name = "legal_id")
    private Long legalEntityId;

    @OneToOne(
            fetch = FetchType.LAZY,
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private EmployeeDetails employeeDetails;
}
