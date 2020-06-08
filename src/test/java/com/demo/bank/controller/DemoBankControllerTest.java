package com.demo.bank.controller;

import com.demo.bank.command.FabrickCommand;
import com.demo.bank.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DemoBankControllerTest {

    @InjectMocks
    private DemoBankController controller;

    @Mock
    private FabrickCommand fabrickCommand;

    @Test
    public void retrieveAccountTransactions() throws URISyntaxException, JsonProcessingException {
        when(fabrickCommand.retrieveTransactions(any(Date.class), any(Date.class), anyBoolean())).thenReturn(mockTransactionsResponse());
        ResponseEntity<TransactionsPayloadDto> response = controller.retrieveAccountTransactions(Date.from(Instant.now()), Date.from(Instant.now()), false);
        TransactionsPayloadDto body = response.getBody();
        Assert.assertNotNull(body);
        Assert.assertEquals(1, body.getList().size());
    }

    @Test
    public void retreiveAccountBalance() throws URISyntaxException, JsonProcessingException {
        when(fabrickCommand.retrieveBalance()).thenReturn(mockBalancesResponse());
        ResponseEntity<BalancePayloadDto> response = controller.retreiveAccountBalance();
        BalancePayloadDto body = response.getBody();
        Assert.assertNotNull(body);
        Assert.assertEquals("EUR", body.getCurrency());
        Assert.assertEquals(new BigDecimal("123.85"), body.getBalance());
    }

    @Test
    public void executeMoneyTransfer() throws URISyntaxException, JsonProcessingException {
        doNothing().when(fabrickCommand).executeMoneyTransfer(any(ExecuteMoneyTransferRequestDto.class));
        ResponseEntity<String> response = controller.executeMoneyTransfer(new ExecuteMoneyTransferRequestDto());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private AccountBalanceResponseDto mockBalancesResponse() {
        AccountBalanceResponseDto responseDto = new AccountBalanceResponseDto();
        responseDto.setStatus("OK");
        BalancePayloadDto payload = new BalancePayloadDto();
        payload.setDate(new Date());
        payload.setAvailableBalance(new BigDecimal("123.85"));
        payload.setBalance(new BigDecimal("123.85"));
        payload.setCurrency("EUR");
        responseDto.setPayload(payload);
        return responseDto;
    }

    private AccountTransactionsResponseDto mockTransactionsResponse() {
        AccountTransactionsResponseDto responseDto = new AccountTransactionsResponseDto();
        responseDto.setStatus("OK");
        TransactionsPayloadDto payload = new TransactionsPayloadDto();
        TransactionDto transaction = new TransactionDto();
        transaction.setAmount(new BigDecimal("123.85"));
        transaction.setCurrency("EUR");
        payload.setList(Lists.newArrayList(transaction));
        responseDto.setPayload(payload);
        return responseDto;
    }
}