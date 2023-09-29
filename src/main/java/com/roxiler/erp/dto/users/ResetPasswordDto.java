package com.roxiler.erp.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {

    @NotBlank(message = "Token should not be blank")
    private String token;

    @NotBlank(message = "New password should not be blank")
    @Size(min = 4, message = "Length of new password should not be less than 4")
    private String newPassword;

    @NotBlank(message = "Confirm new password should not be blank")
    @Size(min = 4, message = "Length of confirm should not be less than 4")
    private String confirmNewPassword;
}
