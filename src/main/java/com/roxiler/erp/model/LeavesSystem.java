package com.roxiler.erp.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "leaves_system")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class LeavesSystem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 5, message = "Leaves should not be more than 50")
    @Column(name = "accrual")
    private Float accrual;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 50, message = "Leaves should not be more than 50")
    @Column(name = "carry_over_limits")
    private Float carryOverLimits;

    @Min(value = 0, message = "Leaves should not be less than 0")
    @Max(value = 20, message = "Leaves should not be more than 20")
    @Column(name = "consecutive_leaves")
    private Float consecutiveLeaves;

    //    @NotBlank(message = "Leave type should not be empty")
//    @Column(name = "allowed_leave_types")
    private String[] allowedLeaveTypes;

    //    @NotBlank(message = "Leave type should not be empty")
//    @Column(name = "allowed_leave_durations")
    private String[] allowedLeaveDurations;

    //     @JsonIdentityInfo(
//             generator = ObjectIdGenerators.PropertyGenerator.class,
//             property = "id")
//    @JsonIgnore
    @JsonIncludeProperties({"id", "name"})
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "organization",
            referencedColumnName = "id",
            nullable = true
    )
    private Organization organization;

    //     @JsonIdentityInfo(
//             generator = ObjectIdGenerators.PropertyGenerator.class,
//             property = "id")
//    @JsonIgnore
    @JsonIncludeProperties({"id", "name"})
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Designation.class
    )
    @JoinColumn(name = "designation", referencedColumnName = "id")
    private Designation designation;
}
