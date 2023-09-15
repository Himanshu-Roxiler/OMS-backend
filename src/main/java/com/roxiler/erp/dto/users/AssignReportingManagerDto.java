package com.roxiler.erp.dto.users;

import jakarta.validation.constraints.Digits;
import lombok.Data;

@Data
public class AssignReportingManagerDto {

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "User ID should not be blank")
    private Integer userId;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Reporting manager ID should not be blank")
    private Integer reportingManagerId;
}
