package com.roxiler.erp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "leaves")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Leaves extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 50, message = "Leaves should not be more than 50")
    @Column(name = "total_leaves")
    private Float totalLeaves;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 50, message = "Leaves should not be more than 50")
    @Column(name = "available_paid_leaves")
    private Float availablePaidLeaves;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 50, message = "Leaves should not be more than 50")
    @Column(name = "available_unpaid_leaves")
    private Float availableUnpaidLeaves;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 50, message = "Leaves should not be more than 50")
    @Column(name = "available_sick_leaves")
    private Float availableSickLeaves;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 50, message = "Leaves should not be more than 50")
    @Column(name = "available_vacation_leaves")
    private Float availableVacationLeaves;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 50, message = "Leaves should not be more than 50")
    @Column(name = "booked_leaves")
    private Float bookedLeaves;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 50, message = "Leaves should not be more than 50")
    @Column(name = "approved_leaves")
    private Float approvedLeaves;

    //     @JsonIdentityInfo(
//             generator = ObjectIdGenerators.PropertyGenerator.class,
//             property = "id")
    @JsonIgnore
//    @OneToOne(
//            mappedBy = "userLeaves",
//            fetch = FetchType.LAZY,
//            cascade = CascadeType.PERSIST,
//            targetEntity = Users.class
//    )
//    @JoinColumn(name = "user_id", referencedColumnName = "userLeaves")
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = true
    )
    private Users user;
}
