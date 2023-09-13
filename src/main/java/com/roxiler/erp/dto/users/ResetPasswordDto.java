package com.roxiler.erp.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {

    @NotBlank(message = "Token should not be blank")
    private String token;

    @NotBlank(message = "New password should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    private String newPassword;

    @NotBlank(message = "Confirm new password should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    private String confirmNewPassword;
}
