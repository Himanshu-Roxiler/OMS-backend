package com.roxiler.erp.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignupDto {
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

}
