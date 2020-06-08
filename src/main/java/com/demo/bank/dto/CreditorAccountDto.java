package com.demo.bank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditorAccountDto {

    private String accountCode;
}
