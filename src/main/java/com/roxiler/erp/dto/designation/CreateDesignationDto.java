package com.roxiler.erp.dto.designation;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDesignationDto {

    @NotBlank(message = "Name should not be blank")
    @Size(min = 3, message = "Length of name should not be less than 3")
    private String name;

    @NotBlank(message = "Description should not be blank")
    @Size(min = 10, message = "Length of description should not be less than 10")
    private String description;
}
