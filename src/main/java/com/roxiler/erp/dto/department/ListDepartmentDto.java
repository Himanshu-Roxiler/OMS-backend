package com.roxiler.erp.dto.department;

import lombok.Data;

@Data
public class ListDepartmentDto {

    private Integer pageNum;
    private Integer pageSize;
    private String sortName;
    private String sortOrder;
}
