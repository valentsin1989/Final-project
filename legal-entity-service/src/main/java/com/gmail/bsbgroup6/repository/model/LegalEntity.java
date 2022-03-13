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
@Table(name = "legal_entities")
public class LegalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "unp", unique = true, nullable = false)
    private Integer unp;

    @Column(name = "iban_by_byn", unique = true, nullable = false)
    private String ibanByByn;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "total_employees", nullable = false)
    private Integer totalEmployees;

    @OneToOne(
            fetch = FetchType.LAZY,
            mappedBy = "legalEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private LegalEntityDates legalEntityDates;
}
