package com.roxiler.erp.dto.leaves;


import com.roxiler.erp.constants.LeaveDurationConstants;
import com.roxiler.erp.constants.TypeOfLeaveConstants;
import com.roxiler.erp.interfaces.Contains;
import lombok.Data;

@Data
public class UpdateLeaveSystemDto {

    private Float accrual;
    private Float consecutiveLeaves;

    private Float carryOverLimits;

    @Contains(
            values = {
                    TypeOfLeaveConstants.PAID_LEAVE,
                    TypeOfLeaveConstants.SICK_LEAVE,
                    TypeOfLeaveConstants.UNPAID_LEAVE,
                    TypeOfLeaveConstants.VACATION_LEAVE
            },
            message = "Invalid input for leave type"
    )
    private String[] allowedLeaveTypes;

    @Contains(
            values = {
                    LeaveDurationConstants.QUARTER_DAY,
                    LeaveDurationConstants.HALF_DAY,
                    LeaveDurationConstants.FULL_DAY
            },
            message = "Invalid input for leave duration"
    )
    private String[] allowedLeaveDurations;
}
