package com.roxiler.erp.interfaces;

import lombok.Data;

import java.util.Date;

@Data
public class ApprovedLeaveBreakup {

    private Date startDate;
    private Date endDate;
    private Float noOfDays;
    private Boolean isApproved = false;

}
