package com.roxiler.erp.dto.leaves;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class ApproveLeaveRequestDto {

    @Temporal(TemporalType.DATE)
    private Date approvedStartDate;

    @Temporal(TemporalType.DATE)
    private Date approvedEndDate;

    private String comment;

    private String note;
}
