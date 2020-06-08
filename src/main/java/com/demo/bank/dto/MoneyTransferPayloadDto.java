package com.demo.bank.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class MoneyTransferPayloadDto implements Serializable {

    private static final long serialVersionUID = -4133448508475437177L;

    private CreditorDto creditor;
    private String description;
    private BigDecimal amount;
    private String currency;
    private String executionDate;
}
