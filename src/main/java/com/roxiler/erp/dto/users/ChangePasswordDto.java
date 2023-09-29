package com.roxiler.erp.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotBlank(message = "Old password should not be blank")
    @Size(min = 4, message = "Length of old password should not be less than 4")
    private String oldPassword;

    @NotBlank(message = "New password should not be blank")
    @Size(min = 4, message = "Length of new password should not be less than 4")
    private String newPassword;

    @NotBlank(message = "Confirm new password should not be blank")
    @Size(min = 4, message = "Length of confirm new password should not be less than 4")
    private String confirmNewPassword;
}
