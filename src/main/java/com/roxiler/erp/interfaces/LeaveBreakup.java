package com.roxiler.erp.interfaces;

import com.roxiler.erp.constants.TypeOfLeaveConstants;
import lombok.Data;

import java.util.Date;

@Data
public class LeaveBreakup {

    private Date startDate;
    private Date endDate;
    private Float noOfDays;
    private Boolean isApproved = false;
}
