package com.demo.bank.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDto implements Serializable {

    private static final long serialVersionUID = 4715241692767949537L;

    private String transactionId;
    private String operationId;
    private Date accountingDate;
    private Date valueDate;
    private TransactionType type;
    private BigDecimal amount;
    private String currency;
    private String description;

    @Override
    public boolean equals(Object o ) {
        return o instanceof TransactionDto && ((TransactionDto) o).getTransactionId().equals(this.transactionId);
    }

}
