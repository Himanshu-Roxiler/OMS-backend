package com.roxiler.erp.dto.holiday;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreateHolidayDto {


    @NotBlank(message = "Holiday Name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String holidayName;

    @NotBlank(message = "Holiday Description should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    private String holidayDescription;

    @NotBlank(message = "Holiday Date should not be blank")
    private Date holidayDate;
}
