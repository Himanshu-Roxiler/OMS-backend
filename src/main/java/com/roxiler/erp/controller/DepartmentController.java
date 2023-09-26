package com.roxiler.erp.controller;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.department.CreateDepartmentDto;
import com.roxiler.erp.dto.department.ListDepartmentDto;
import com.roxiler.erp.dto.department.UpdateDepartmentDto;
import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.ApplicationScope;

@RestController()
@RequestMapping("/v1/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

//    @GetMapping("/all-departments")
//    public ResponseEntity<ResponseObject> getAllDepartments() {
//
//        Iterable<Department> departments = departmentService.getAllDepartments();
//        ResponseObject responseObject = new ResponseObject();
//        responseObject.setIs_success(true);
//        responseObject.setMessage("Successfully fetched departments");
//        responseObject.setData(departments);
//        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
//
//        return response;
//    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getListDepartmentsWithOrg(
            @AuthenticationPrincipal UserDto userDto,
//            @RequestBody ListDepartmentDto listDepartmentDto,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortName", defaultValue = "id") String sortName,
            @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {
        Iterable<Department> departments = departmentService.getListDepartmentsWithPagination(userDto, pageNum, pageSize, sortName, sortOrder, search);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched departments");
        responseObject.setData(departments);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addDepartment(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody CreateDepartmentDto department
    ) {
        Department dept = departmentService.saveDepartment(department, userDto.getOrgId());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created department");
        responseObject.setData(dept);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateDepartment(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody UpdateDepartmentDto department,
            @PathVariable("id") Integer id
    ) {

        Department dept = departmentService.updateDepartment(department, id, userDto.getEmail());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated department");
        responseObject.setData(dept);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteDepartment(
            @AuthenticationPrincipal UserDto userDto,
            @PathVariable("id") Integer id
    ) throws Exception {
        departmentService.deleteDepartment(id, userDto.getEmail());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted department");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getDepartment(@PathVariable("id") Integer id) {

        Department department = departmentService.getDepartmentById(id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched department");
        responseObject.setData(department);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

}
