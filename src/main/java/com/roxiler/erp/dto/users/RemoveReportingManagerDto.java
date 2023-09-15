package com.roxiler.erp.dto.users;

import jakarta.validation.constraints.Digits;
import lombok.Data;

@Data
public class RemoveReportingManagerDto {

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "User ID should not be blank")
    private Integer userId;

}
