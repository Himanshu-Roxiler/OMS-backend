package com.roxiler.erp.dto.leaves;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.roxiler.erp.constants.TypeOfLeaveConstants;
import com.roxiler.erp.interfaces.LeaveBreakup;
import com.roxiler.erp.interfaces.ValidateString;
import com.roxiler.erp.model.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class CreateLeaveTrackerDto {

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    private LeaveBreakup[] leaveBreakups;

    @NotBlank(message = "Reason should not be blank")
    @Size(min = 10, message = "Reason should not be less than 10 letters")
    private String reason;

    private Float noOfDays;

    @NotBlank(message = "Type of leave should not be blank")
    @Size(min = 10, message = "Type of leave should not be less than 10 letters")
    @ValidateString(
            acceptedValues = {
                    TypeOfLeaveConstants.PAID_LEAVE,
                    TypeOfLeaveConstants.SICK_LEAVE,
                    TypeOfLeaveConstants.UNPAID_LEAVE,
                    TypeOfLeaveConstants.VACATION_LEAVE
            },
            message = "Invalid input for leave type"
    )
    private String typeOfLeave;
}
