package com.roxiler.erp.dto.users;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUsersDto {

    @NotBlank(message = "First name should not be blank")
    @Size(min = 2, message = "Length of first name should not be less than 2")
    private String firstName;

    @NotBlank(message = "Last name should not be blank")
    @Size(min = 2, message = "Length of last name should not be less than 2")
    private String lastName;

    @NotBlank(message = "Username should not be blank")
    @Size(min = 3, message = "Length of username should not be less than 3")
    private String username;

    @NotBlank(message = "Email should not be blank")
    @Size(min = 3, message = "Length of email should not be less than 3")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email")
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min = 4, message = "Length of password should not be less than 4")
    private String password;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Department should not be blank")
    private Integer deptId;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Designation should not be blank")
    private Integer desgId;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Role should not be blank")
    private Integer roleId;
}
