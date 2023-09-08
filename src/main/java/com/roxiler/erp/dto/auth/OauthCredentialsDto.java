package com.roxiler.erp.dto.auth;

import lombok.Data;

@Data
public class OauthCredentialsDto {

    private String accessToken;

    private String oauthClient;
}
