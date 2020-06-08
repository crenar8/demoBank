package com.demo.bank.service.impl;

import com.demo.bank.data.TransactionEntity;
import com.demo.bank.data.TransactionRepository;
import com.demo.bank.dto.AccountTransactionsResponseDto;
import com.demo.bank.dto.TransactionDto;
import com.demo.bank.service.CallFabrickService;
import com.demo.bank.service.RetrieveTransactionStrategy;
import com.demo.bank.util.transformer.TransactionEntityTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service("basicRetrieveTransactionStrategyImpl")
public class BasicRetrieveTransactionStrategyImpl implements RetrieveTransactionStrategy {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final String RETRIEVE_TRANSACTIONS_ENDPOINT = "/api/gbs/banking/v4.0/accounts/{accountId}/transactions";
    private static final TransactionEntityTransformer TRANSACTION_ENTITY_TRANSFORMER = new TransactionEntityTransformer();

    private final ObjectMapper objectMapper;
    private final CallFabrickService callFabrickService;
    private final TransactionRepository transactionRepository;

    @Override
    public AccountTransactionsResponseDto retreiveTransactions(Long accountId, Date dateFrom, Date dateTo) throws URISyntaxException, JsonProcessingException {
        ResponseEntity<String> responseEntity = callFabrickService.get(StringUtils.join(replaceAccountId(accountId.toString()), "?fromAccountingDate=", DATE_FORMATTER.format(dateFrom), "&toAccountingDate=", DATE_FORMATTER.format(dateTo)));
        AccountTransactionsResponseDto responseDto = objectMapper.readValue(responseEntity.getBody(), AccountTransactionsResponseDto.class);
        storeTransactions(responseDto.getPayload().getList(), accountId);
        return responseDto;
    }

    private void storeTransactions(List<TransactionDto> list, Long accountId) {
        List<TransactionEntity> transactionsToStore = new ArrayList<>();
        TRANSACTION_ENTITY_TRANSFORMER.setAccountId(accountId);
        list.forEach(transactionDto -> {
            cleanExistingTransactions(transactionDto);
            transactionsToStore.add(TRANSACTION_ENTITY_TRANSFORMER.transform(transactionDto));
        });
        transactionRepository.saveAll(transactionsToStore);
    }

    private void cleanExistingTransactions(TransactionDto transactionDto) {
        List<TransactionEntity> existingTransactions = transactionRepository.findAll(Example.of(transactionEntityWithTransactionId(transactionDto.getTransactionId())));
        if (CollectionUtils.isNotEmpty(existingTransactions)) {
            transactionRepository.deleteInBatch(existingTransactions);
        }
    }

    private TransactionEntity transactionEntityWithTransactionId(String transactionId) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionId(transactionId);
        return transaction;
    }

    private String replaceAccountId(String accountId) {
        return StringUtils.replace(RETRIEVE_TRANSACTIONS_ENDPOINT, "{accountId}", accountId);
    }

}
