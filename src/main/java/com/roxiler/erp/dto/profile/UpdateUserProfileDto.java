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
    @Size(min = 2, message = "Length of first name should not be less than 2")
    private String firstName;

    @NotBlank(message = "First name should not be blank")
    @Size(min = 2, message = "Length of last name should not be less than 2")
    private String lastName;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String gender;

    @NotBlank(message = "First name should not be blank")
    @Size(min = 3, message = "Length of work phone should not be less than 3")
    private String workPhoneNumber;

    private String personalPhoneNumber;

    private String aadharCard;

    private String panCard;

    private String monthlyCtc;

    private String language;
}
