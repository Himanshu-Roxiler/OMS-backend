package com.roxiler.erp.dto.roles;

import lombok.Data;

@Data
public class ListRolesDto {
    private Integer pageNum;
    private Integer pageSize;
    private String sortName;
    private String sortOrder;

}
