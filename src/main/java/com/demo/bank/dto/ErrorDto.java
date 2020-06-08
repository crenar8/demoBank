package com.demo.bank.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorDto implements Serializable {

    private static final long serialVersionUID = 2966825613714138059L;

    private String code;
    private String description;
    private String params;
}
