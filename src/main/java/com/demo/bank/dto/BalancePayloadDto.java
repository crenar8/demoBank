package com.demo.bank.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BalancePayloadDto implements Serializable {

    private static final long serialVersionUID = -2912342146706156522L;

    private Date date;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private String currency;
}
