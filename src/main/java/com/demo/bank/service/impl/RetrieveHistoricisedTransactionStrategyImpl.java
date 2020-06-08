package com.demo.bank.service.impl;

import com.demo.bank.data.TransactionEntity;
import com.demo.bank.data.TransactionRepository;
import com.demo.bank.dto.AccountTransactionsResponseDto;
import com.demo.bank.dto.TransactionDto;
import com.demo.bank.dto.TransactionsPayloadDto;
import com.demo.bank.service.RetrieveTransactionStrategy;
import com.demo.bank.util.transformer.TransactionDtoTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("historicisedRetrieveTransactionStrategyImpl")
public class RetrieveHistoricisedTransactionStrategyImpl implements RetrieveTransactionStrategy {

    private static final Transformer<TransactionEntity, TransactionDto> ENTITY_TRANSACTION_DTO_TRANSFORMER = new TransactionDtoTransformer();

    private final TransactionRepository transactionRepository;


    @Override
    public AccountTransactionsResponseDto retreiveTransactions(Long accountId, Date dateFrom, Date dateTo) throws URISyntaxException, JsonProcessingException {
        AccountTransactionsResponseDto responseDto = new AccountTransactionsResponseDto();
        responseDto.setStatus("OK");
        TransactionsPayloadDto payload = new TransactionsPayloadDto();
        List<TransactionEntity> entities = transactionRepository.findAll(Example.of(transactionWithAccountId(accountId)));
        List<TransactionEntity> transactionEntities = entities.stream().filter(transactionEntity -> {
            Date accountingDate = transactionEntity.getAccountingDate();
            return accountingDate.compareTo(dateFrom) >= NumberUtils.INTEGER_ZERO && accountingDate.compareTo(dateTo) <= NumberUtils.INTEGER_ZERO;
        }).collect(Collectors.toList());
        payload.setList(transactionEntities.stream().map(ENTITY_TRANSACTION_DTO_TRANSFORMER::transform).collect(Collectors.toList()));
        responseDto.setPayload(payload);
        return responseDto;
    }

    private TransactionEntity transactionWithAccountId(Long accountId) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setAccountId(accountId);
        return transaction;
    }
}
