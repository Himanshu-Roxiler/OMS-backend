package com.roxiler.erp.dto.users;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {

    @NotBlank(message = "First name should not be blank")
    @Size(min = 2, message = "Length of first name should not be less than 2")
    private String firstName;

    @NotBlank(message = "Last name should not be blank")
    @Size(min = 2, message = "Length of last name should not be less than 2")
    private String lastName;

    @NotBlank(message = "Username should not be blank")
    @Size(min = 3, message = "Length of username should not be less than 3")
    private String username;

//    @NotBlank(message = "Email should not be blank")
//    @Size(min=3, message = "Length should not be less than 3")
//    private String email;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Department should not be blank")
    private Integer deptId;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Designation should not be blank")
    private Integer desgId;
}
