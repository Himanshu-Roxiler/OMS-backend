package com.roxiler.erp.dto.profile;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateUserProfileDto {

    @NotBlank(message = "First name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String firstName;

    @NotBlank(message = "First name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String lastName;

    @NotBlank(message = "DOB should not be blank")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String gender;

    @NotBlank(message = "First name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String workPhoneNumber;

    private String personalPhoneNumber;

    private String aadharCard;

    private String panCard;

    private String monthlyCtc;

    private String language;
}
