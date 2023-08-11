package com.roxiler.erp.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDepartmentDto {
    @NotBlank(message = "Name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String name;

    @NotBlank(message = "Description should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String description;

}
