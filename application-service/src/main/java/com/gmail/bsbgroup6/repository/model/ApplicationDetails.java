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
@Table(name = "application_details")
public class ApplicationDetails {

    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "application")
    )
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "application_id", unique = true, nullable = false)
    private Long applicationId;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Application application;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "last_update")
    private String lastUpdate;

    @Column(name = "note")
    private String note;

    public ApplicationDetails() {
    }

    public ApplicationDetails(Application application) {
        this.application = application;
        application.setApplicationDetails(this);
    }
}
