package com.demo.bank.service;

import com.demo.bank.dto.AccountTransactionsResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.URISyntaxException;
import java.util.Date;

public interface RetrieveTransactionStrategy {

    AccountTransactionsResponseDto retreiveTransactions(Long accountId, Date dateFrom, Date dateTo) throws URISyntaxException, JsonProcessingException;
}
