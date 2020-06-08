package com.demo.bank.command;

import com.demo.bank.dto.AccountBalanceResponseDto;
import com.demo.bank.dto.AccountTransactionsResponseDto;
import com.demo.bank.dto.ExecuteMoneyTransferRequestDto;
import com.demo.bank.dto.MoneyTransferPayloadDto;
import com.demo.bank.service.CallFabrickService;
import com.demo.bank.service.RetrieveTransactionStrategy;
import com.demo.bank.util.transformer.MoneyTransferRequestTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FabrickCommand {

    private static final String RETRIEVE_TRANSACTIONS_SUFFIX = "RetrieveTransactionStrategyImpl";
    private static final String RETRIEVE_BALANCE_ENDPOINT = "/api/gbs/banking/v4.0/accounts/{accountId}/balance";
    private static final String EXECUTE_MONEY_TRANSFER_ENDPOINT = "/api/gbs/banking/v4.0/accounts/{accountId}/payments/money-transfers";
    private static final Transformer<ExecuteMoneyTransferRequestDto, MoneyTransferPayloadDto> MONEY_TRANSFER_REQUEST_TRANSFORMER = new MoneyTransferRequestTransformer();


    private final ObjectMapper objectMapper;
    private final CallFabrickService callFabrickService;
    private final Map<String, RetrieveTransactionStrategy> retrieveTransactionStrategyMap;

    @Value("${fabrick.account.id}")
    private Long accountId;

    public AccountBalanceResponseDto retrieveBalance() throws URISyntaxException, JsonProcessingException {
        ResponseEntity<String> response = callFabrickService.get(replaceAccountId(RETRIEVE_BALANCE_ENDPOINT));
        return objectMapper.readValue(response.getBody(), AccountBalanceResponseDto.class);
    }

    public AccountTransactionsResponseDto retrieveTransactions(Date from, Date to, boolean historicised) throws URISyntaxException, JsonProcessingException {
        RetrieveTransactionStrategy retrieveTransactionStrategy = resolveStrategy(historicised);
        return retrieveTransactionStrategy.retreiveTransactions(accountId, from, to);
    }

    public void executeMoneyTransfer(ExecuteMoneyTransferRequestDto requestDto) throws URISyntaxException, JsonProcessingException {
        callFabrickService.post(replaceAccountId(EXECUTE_MONEY_TRANSFER_ENDPOINT), MONEY_TRANSFER_REQUEST_TRANSFORMER.transform(requestDto));
    }

    private String replaceAccountId(String string){
        return StringUtils.replace(string, "{accountId}", accountId.toString());
    }


    private RetrieveTransactionStrategy resolveStrategy(boolean historicised) {
        String key = historicised ? "historicised" : "basic";
        return retrieveTransactionStrategyMap.get(StringUtils.join(key, RETRIEVE_TRANSACTIONS_SUFFIX));
    }
}
