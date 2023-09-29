package com.roxiler.erp.dto.roles;

import com.roxiler.erp.model.Feature;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.UserOrganizationRole;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class CreateUserRoleDto {
    @NotBlank(message = "Role name should not be blank")
    @Size(min = 3, message = "Length of name should not be less than 3")
    private String name;

    private Integer[] featureIds;
}
