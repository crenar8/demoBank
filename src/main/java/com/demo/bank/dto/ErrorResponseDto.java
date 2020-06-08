package com.demo.bank.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ErrorResponseDto implements Serializable {

    private static final long serialVersionUID = 5714179327301987233L;

    private String status;
    private List<ErrorDto> errors;
}
