package com.demo.bank.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TransactionsPayloadDto implements Serializable {

    private static final long serialVersionUID = 7315240770086165548L;

    private List<TransactionDto> list;
}
