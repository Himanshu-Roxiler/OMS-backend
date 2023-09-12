package com.roxiler.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.holiday.CreateHolidayDto;
import com.roxiler.erp.dto.holiday.UpdateHolidayDto;
import com.roxiler.erp.model.Holiday;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.HolidayService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/holiday")
public class HolidayController {
    
    @Autowired
    private HolidayService Holidayervice;

    @GetMapping("/all-holiday")
    public ResponseEntity<ResponseObject> getAllHolidays(@AuthenticationPrincipal UserDto userDto) {

        Iterable<Holiday> Holidays = Holidayervice.getHolidayListFromOrg(userDto);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched departments");
        responseObject.setData(Holidays);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllHolidaysFromOrg(@AuthenticationPrincipal UserDto userDto) {

        Iterable<Holiday> Holidays = Holidayervice.getHolidayListFromOrg(userDto);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched departments");
        responseObject.setData(Holidays);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addHoliday(@AuthenticationPrincipal UserDto userDto ,@Valid @RequestBody CreateHolidayDto Holiday) {

        Holiday Holiday2 = Holidayervice.saveHoliday(Holiday,userDto);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created Holiday.");
        responseObject.setData(Holiday2);

        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateHoliday(@Valid @RequestBody UpdateHolidayDto Holiday, @AuthenticationPrincipal UserDto userDto) {

        Holiday Holiday2 = Holidayervice.updateHoliday(Holiday, userDto.getId());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated Holiday");
        responseObject.setData(Holiday2);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteHoliday(@PathVariable("id") Integer id) {

        Holidayervice.deleteHoliday(id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted Holiday.");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

}
