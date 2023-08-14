package com.roxiler.erp.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignupDto {
    @NotBlank(message = "First name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String firstName;

    @NotBlank(message = "Last name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String lastName;

    @NotBlank(message = "Username should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String username;

    @NotBlank(message = "Email should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String password;

}
