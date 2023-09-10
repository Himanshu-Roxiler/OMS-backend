package com.roxiler.erp.dto.users;

import lombok.Data;

@Data
public class ListUsersDto {

    private Integer pageNum;
    private Integer pageSize;
    private String sortName;
    private String sortOrder;
}
