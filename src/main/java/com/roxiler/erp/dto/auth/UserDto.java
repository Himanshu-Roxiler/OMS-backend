package com.roxiler.erp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private Integer id;
    private String username;
    private String email;
    private Integer orgId;
    private String login;
    private String token;
}
