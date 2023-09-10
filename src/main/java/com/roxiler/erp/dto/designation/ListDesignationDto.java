package com.roxiler.erp.dto.designation;

import lombok.Data;

@Data
public class ListDesignationDto {
    private Integer pageNum;
    private Integer pageSize;
    private String sortName;
    private String sortOrder;
}
