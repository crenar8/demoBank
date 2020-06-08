package com.demo.bank.command;

import com.demo.bank.dto.*;
import com.demo.bank.service.CallFabrickService;
import com.demo.bank.service.RetrieveTransactionStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FabrickCommandTest {

    @InjectMocks
    private FabrickCommand fabrickCommand;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CallFabrickService callFabrickService;

    @Mock
    private Map<String, RetrieveTransactionStrategy> retrieveTransactionStrategyMap;

    @Mock
    private RetrieveTransactionStrategy retrieveTransactionStrategy;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(fabrickCommand, "accountId", 1234567L);
    }

    @Test
    public void retrieveBalance() throws URISyntaxException, JsonProcessingException {
        when(callFabrickService.get(anyString())).thenReturn(mockResponse());
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(getAccountBalanceResponseDto());
        AccountBalanceResponseDto balance = fabrickCommand.retrieveBalance();
        Assert.assertNotNull(balance);
        Assert.assertEquals(new BigDecimal("342.56"), balance.getPayload().getAvailableBalance());
    }

    @Test
    public void retrieveTransactions() throws URISyntaxException, JsonProcessingException {
        when(retrieveTransactionStrategyMap.get(anyString())).thenReturn(retrieveTransactionStrategy);
        AccountTransactionsResponseDto accountTransactionsDto = new AccountTransactionsResponseDto();
        accountTransactionsDto.setStatus("OK");
        TransactionsPayloadDto payload = new TransactionsPayloadDto();
        TransactionDto transaction = new TransactionDto();
        payload.setList(Lists.newArrayList(transaction));
        accountTransactionsDto.setPayload(payload);
        when(retrieveTransactionStrategy.retreiveTransactions(anyLong(), any(Date.class), any(Date.class))).thenReturn(mockTransactionsResponse());
        AccountTransactionsResponseDto responseDto = fabrickCommand.retrieveTransactions(new Date(), new Date(), false);
        Assert.assertNotNull(responseDto);
        Assert.assertEquals(1, responseDto.getPayload().getList().size() );
    }

    @Test
    public void executeMoneyTransfer() throws URISyntaxException, JsonProcessingException {
        doNothing().when(callFabrickService).post(anyString(), any());
        fabrickCommand.executeMoneyTransfer(mockRequest());
    }

    private ExecuteMoneyTransferRequestDto mockRequest() {
        ExecuteMoneyTransferRequestDto requestDto = new ExecuteMoneyTransferRequestDto();
        requestDto.setAccountId(1234567L);
        requestDto.setAmount("342.56");
        requestDto.setCurrency("EUR");
        requestDto.setDescription("fake description");
        requestDto.setExecutionDate(new Date());
        requestDto.setReceiverName("Giuseppe Bonaccorso");
        return requestDto;
    }

    private ResponseEntity<String> mockResponse() throws JsonProcessingException {
        AccountBalanceResponseDto mockBalance = getAccountBalanceResponseDto();
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(mockBalance));
    }

    private AccountBalanceResponseDto getAccountBalanceResponseDto() {
        AccountBalanceResponseDto mockBalance = new AccountBalanceResponseDto();
        mockBalance.setStatus("OK");
        BalancePayloadDto payload = new BalancePayloadDto();
        payload.setCurrency("EUR");
        payload.setBalance(new BigDecimal("342.56"));
        payload.setAvailableBalance(new BigDecimal("342.56"));
        payload.setDate(new Date());
        mockBalance.setPayload(payload);
        return mockBalance;
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