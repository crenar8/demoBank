package com.demo.bank.util.transformer;

import com.demo.bank.dto.CreditorAccountDto;
import com.demo.bank.dto.CreditorDto;
import com.demo.bank.dto.ExecuteMoneyTransferRequestDto;
import com.demo.bank.dto.MoneyTransferPayloadDto;
import org.apache.commons.collections4.Transformer;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class MoneyTransferRequestTransformer implements Transformer<ExecuteMoneyTransferRequestDto, MoneyTransferPayloadDto> {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public MoneyTransferPayloadDto transform(ExecuteMoneyTransferRequestDto executeMoneyTransferRequestDto) {
        return MoneyTransferPayloadDto.builder()
                .amount(new BigDecimal(executeMoneyTransferRequestDto.getAmount()))
                .currency(executeMoneyTransferRequestDto.getCurrency())
                .description(executeMoneyTransferRequestDto.getDescription())
                .executionDate(DATE_FORMATTER.format(executeMoneyTransferRequestDto.getExecutionDate()))
                .creditor(CreditorDto.builder()
                        .account(CreditorAccountDto.builder()
                                .accountCode(executeMoneyTransferRequestDto.getAccountId().toString())
                                .build())
                        .name(executeMoneyTransferRequestDto.getReceiverName())
                        .build())
                .build();
    }
}
