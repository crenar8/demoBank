package com.demo.bank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditorDto {

    private String name;
    private CreditorAccountDto account;
}
