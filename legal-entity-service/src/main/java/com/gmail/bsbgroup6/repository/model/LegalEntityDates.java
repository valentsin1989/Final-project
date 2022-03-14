package com.gmail.bsbgroup6.repository.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "legal_entity_dates")
public class LegalEntityDates {

    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "legalEntity")
    )
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "legal_id", unique = true, nullable = false)
    private Long legalId;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private LegalEntity legalEntity;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "last_update")
    private String lastUpdate;

    public LegalEntityDates() {
    }

    public LegalEntityDates(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
        legalEntity.setLegalEntityDates(this);
    }
}
