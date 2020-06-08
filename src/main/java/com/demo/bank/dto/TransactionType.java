package com.demo.bank.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionType implements Serializable {

    private static final long serialVersionUID = 8846909183444768635L;

    private String enumeration;
    private String value;
}
