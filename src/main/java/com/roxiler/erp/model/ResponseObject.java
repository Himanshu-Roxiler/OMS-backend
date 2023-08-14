package com.roxiler.erp.model;

import lombok.Data;

@Data
public class ResponseObject {

    private Boolean is_success;

    private String message;

    private Object data;
}
