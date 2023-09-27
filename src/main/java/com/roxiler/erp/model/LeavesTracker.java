package com.roxiler.erp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@Table(name = "leaves_tracker")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class LeavesTracker extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = true)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = true)
    private Date endDate;

    @NotBlank(message = "Reason should not be blank")
    @Size(min = 10, message = "Length should not be less than 3")
    @Column(name = "reason")
    private String reason;

    @NotBlank(message = "Type of leave should not be blank")
    @Size(min = 10, message = "Length should not be less than 3")
    @Column(name = "type_of_leave")
    private String typeOfLeave;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "note", nullable = true)
    private String note;

    @Column(name = "leave_cancel_reason", nullable = true)
    private String leaveCancelReason;

    @Column(name = "is_approved", nullable = true)
    private Boolean isApproved;

    @Column(name = "is_cancelled")
    private Boolean isCancelled = false;

    @Temporal(TemporalType.DATE)
    @Column(name = "approved_start_date", nullable = true)
    private Date approvedStartDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "approved_end_date", nullable = true)
    private Date approvedEndDate;

    @Column(name = "no_of_days")
    private Float noOfDays;

    @Column(name = "reporting_manager", nullable = true)
    private Integer reportingManager;

    @Column(name = "user_id", nullable = true)
    private Integer userId;

    @ColumnTransformer(write = "?::jsonb")
    @Column(name = "leave_breakups", nullable = true, columnDefinition = "jsonb")
    private String leaveBreakups;

    @ColumnTransformer(write = "?::jsonb")
    @Column(name = "approved_leave_breakups", nullable = true, columnDefinition = "jsonb")
    private String approvedLeaveBreakups;
}
