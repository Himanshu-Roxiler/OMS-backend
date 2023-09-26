package com.roxiler.erp.dto.leaves;


import lombok.Data;

@Data
public class UpdateLeaveSystemDto {

    private Float accrual;
    private Float consecutiveLeaves;

    private Float carryOverLimits;

    private String[] allowedLeaveTypes;

    private String[] allowedLeaveDurations;

    private Integer leavePolicyId;
}
