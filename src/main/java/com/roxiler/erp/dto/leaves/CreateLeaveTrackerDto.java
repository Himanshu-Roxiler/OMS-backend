package com.roxiler.erp.dto.leaves;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

    @NotBlank(message = "Reason should not be blank")
    @Size(min = 10, message = "Length should not be less than 3")
    private String reason;

    @NotBlank(message = "Type of leave should not be blank")
    @Size(min = 10, message = "Length should not be less than 3")
    private String typeOfLeave;
}
