package com.demo.bank.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "operation_id")
    private String operationId;

    @Column(name = "accounting_date")
    private Date accountingDate;

    @Column(name = "value_date")
    private Date valueDate;

    @Column(name = "type_enumeration")
    private String typeEnumeration;

    @Column(name = "type_value")
    private String typeValue;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "description")
    private String description;

}
