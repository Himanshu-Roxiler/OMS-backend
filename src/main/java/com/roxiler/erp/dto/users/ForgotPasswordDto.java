package com.roxiler.erp.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgotPasswordDto {

    @NotBlank(message = "Email should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    private String email;
}
