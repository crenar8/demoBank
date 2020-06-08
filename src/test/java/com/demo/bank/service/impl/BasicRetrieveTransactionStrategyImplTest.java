package com.demo.bank.service.impl;

import com.demo.bank.data.TransactionEntity;
import com.demo.bank.data.TransactionRepository;
import com.demo.bank.dto.AccountTransactionsResponseDto;
import com.demo.bank.dto.TransactionDto;
import com.demo.bank.dto.TransactionsPayloadDto;
import com.demo.bank.service.CallFabrickService;
import com.demo.bank.util.transformer.TransactionEntityTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicRetrieveTransactionStrategyImplTest {

    @InjectMocks
    private BasicRetrieveTransactionStrategyImpl basicRetrieveTransactionStrategy;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CallFabrickService callFabrickService;
    @Mock
    private TransactionRepository transactionRepository;


    @Test
    public void retreiveTransactions() throws URISyntaxException, JsonProcessingException {
        AccountTransactionsResponseDto value = mockTransactionsResponse();
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(value);
        when(callFabrickService.get(anyString())).thenReturn(ResponseEntity.ok(new ObjectMapper().writeValueAsString(value)));
        when(transactionRepository.findAll(any(Example.class))).thenReturn(mockTransactionEntities());
        when(transactionRepository.saveAll(any(Iterable.class))).thenReturn(mockTransactionEntities());
        doNothing().when(transactionRepository).deleteInBatch(any(List.class));
        AccountTransactionsResponseDto transactions = basicRetrieveTransactionStrategy.retreiveTransactions(1234567L, new Date(), new Date());
        Assert.assertNotNull(transactions);
        Assert.assertEquals("OK", transactions.getStatus());
        Assert.assertEquals(1, transactions.getPayload().getList().size());
        Assert.assertEquals("EUR", transactions.getPayload().getList().get(0).getCurrency());
        Assert.assertEquals(new BigDecimal("123.85"), transactions.getPayload().getList().get(0).getAmount());
    }

    private List<TransactionEntity> mockTransactionEntities() {
        TransactionEntityTransformer transformer = new TransactionEntityTransformer();
        transformer.setAccountId(1234567L);
        return mockTransactionsResponse().getPayload().getList().stream().map(transformer::transform).collect(Collectors.toList());
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