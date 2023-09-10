package com.roxiler.erp.dto.users;

import com.roxiler.erp.constants.GenericConstants;
import com.roxiler.erp.interfaces.ValidateString;
import lombok.Data;

@Data
public class ListUsersDto {

    private Integer pageNum;
    private Integer pageSize;
    private String sortName;

    @ValidateString(
            acceptedValues = {
                    GenericConstants.ASCENDING,
                    GenericConstants.DESCENDING
            },
            message = "Invalid input for leave type"
    )
    private String sortOrder;
}
