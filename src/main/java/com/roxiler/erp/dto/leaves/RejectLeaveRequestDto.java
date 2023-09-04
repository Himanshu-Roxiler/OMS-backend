package com.roxiler.erp.dto.leaves;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class RejectLeaveRequestDto {
    @NotBlank(message = "Comment should not be blank")
    @Size(min = 10, message = "Length should not be less than 10")
    private String comment;

    private String note;
}
