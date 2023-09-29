package com.roxiler.erp.dto.roles;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserRoleDto {

    @NotBlank(message = "Role name should not be blank")
    @Size(min = 3, message = "Length of name should not be less than 3")
    private String name;

    //private Integer[] featureIds;
}
