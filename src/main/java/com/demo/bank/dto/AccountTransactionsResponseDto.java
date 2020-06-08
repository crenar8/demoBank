package com.demo.bank.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountTransactionsResponseDto implements Serializable {

    private static final long serialVersionUID = 4872205972168045360L;

    private String status;
    private TransactionsPayloadDto payload;
}
