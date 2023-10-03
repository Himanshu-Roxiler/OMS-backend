package com.roxiler.erp.controller;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Leaves;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.service.DepartmentService;
import com.roxiler.erp.service.HolidayService;
import com.roxiler.erp.service.LeaveService;
import com.roxiler.erp.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/v1/dashboard")
public class DashboardController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private LeaveService leaveService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getListDepartmentsWithOrg(
            @AuthenticationPrincipal UserDto userDto
    ) {
        Iterable<Map<String, Object>> upcomingHolidays = holidayService.upcomingHolidaysWithPagination(userDto);
        List<Map<String, Object>> newJoinees = usersService.getNewJoinees(userDto);
        Leaves userLeaves = leaveService.getUserLeaves(userDto);
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("upcomingHolidays", upcomingHolidays);
        dashboardData.put("newJoinees", newJoinees);
        dashboardData.put("leaveReport", userLeaves);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched dashboard data");
        responseObject.setData(dashboardData);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

}
