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
@Table(name = "employee_details")
public class EmployeeDetails {

    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "employee")
    )
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "employee_id", unique = true, nullable = false)
    private Long employeeId;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Employee employee;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "last_update")
    private String lastUpdate;

    @Column(name = "position_by_legal")
    private String positionByLegal;

    @Column(name = "note")
    private String note;

    public EmployeeDetails() {
    }

    public EmployeeDetails(Employee employee) {
        this.employee = employee;
        employee.setEmployeeDetails(this);
    }
}
