package com.roxiler.erp.dto.holiday;

import java.util.Date;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreateHolidayDto {


    @NotBlank(message = "Holiday Name should not be blank")
    @Size(min = 3, message = "Length of name should not be less than 3")
    private String holidayName;

    @NotBlank(message = "Holiday Description should not be blank")
    @Size(min = 10, message = "Length of description should not be less than 10")
    private String holidayDescription;

    @Temporal(TemporalType.DATE)
    private Date holidayDate;
}
