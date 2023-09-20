package com.roxiler.erp.interfaces;

import lombok.Data;

import java.util.Date;

@Data
public class LeaveBreakup {

    private Date startDate;
    private Date endDate;
    private Float noOfDays;
    private String timeOfDay;
    private Boolean isApproved = false;
}
