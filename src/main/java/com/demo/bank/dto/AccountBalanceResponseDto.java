package com.demo.bank.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountBalanceResponseDto implements Serializable {

    private static final long serialVersionUID = -8564854925864121247L;

    private String status;
    private BalancePayloadDto payload;
}
