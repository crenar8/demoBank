package com.demo.bank.service.impl;

import com.demo.bank.data.TransactionEntity;
import com.demo.bank.data.TransactionRepository;
import com.demo.bank.dto.AccountTransactionsResponseDto;
import com.demo.bank.dto.TransactionDto;
import com.demo.bank.dto.TransactionsPayloadDto;
import com.demo.bank.util.transformer.TransactionEntityTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveHistoricisedTransactionStrategyImplTest {

    @InjectMocks
    private RetrieveHistoricisedTransactionStrategyImpl retrieveHistoricisedTransactionStrategy;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void retreiveTransactions() throws URISyntaxException, JsonProcessingException {
        when(transactionRepository.findAll(any(Example.class))).thenReturn(mockTransactionEntities());
        Calendar calendarFrom = new GregorianCalendar(2014, Calendar.FEBRUARY, 11);
        Date dateFrom = calendarFrom.getTime();
        Calendar calendarTo = new GregorianCalendar(2074, Calendar.NOVEMBER, 11);
        Date dateTo = calendarTo.getTime();
        AccountTransactionsResponseDto response = retrieveHistoricisedTransactionStrategy.retreiveTransactions(1234567L, dateFrom, dateTo);
        Assert.assertNotNull(response);
        Assert.assertEquals("OK", response.getStatus());
        Assert.assertEquals(1, response.getPayload().getList().size());
        Assert.assertEquals("EUR", response.getPayload().getList().get(0).getCurrency());
        Assert.assertEquals(new BigDecimal("125.85"), response.getPayload().getList().get(0).getAmount());
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
        transaction.setAmount(new BigDecimal("125.85"));
        transaction.setAccountingDate(new Date());
        transaction.setCurrency("EUR");
        payload.setList(Lists.newArrayList(transaction));
        responseDto.setPayload(payload);
        return responseDto;
    }

}