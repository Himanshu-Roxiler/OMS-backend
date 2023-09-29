package com.roxiler.erp.dto.leaves;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CancelLeaveRequestDto {
    @NotBlank(message = "Reason should not be blank")
    @Size(min = 10, message = "Length of reason should not be less than 10")
    private String leaveCancelReason;

}
