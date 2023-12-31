package com.roxiler.erp.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "holiday")
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class Holiday extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "holiday_name")
    private String holidayName;

    @Column(name = "holiday_description")
    private String holidayDescription;

    @Column(name = "holiday_date")
    private Date holidayDate;

    //    @JsonIgnore
    @JsonIncludeProperties({"id", "name"})
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "organization_id",
            referencedColumnName = "id",
            nullable = true
    )
    private Organization organization;


}
