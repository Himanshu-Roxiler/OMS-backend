package com.roxiler.erp.dto.leaves;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CancelLeaveRequestDto {
    @NotBlank(message = "Reason should not be blank")
    @Size(min = 10, message = "Length should not be less than 3")
    private String leaveCancelReason;

}
