package com.demo.bank.controller;

import com.demo.bank.command.FabrickCommand;
import com.demo.bank.dto.*;
import com.demo.bank.service.CallFabrickService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/demo/bridge", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoBankController {

    private final FabrickCommand fabrickCommand;

    @GetMapping(path = "/account/transactions")
    public ResponseEntity<TransactionsPayloadDto> retrieveAccountTransactions(@RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromAccountingDate, @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toAccountingDate, @RequestParam(required = false, defaultValue = "false") boolean historicised) throws URISyntaxException, JsonProcessingException {
        return ResponseEntity.ok(fabrickCommand.retrieveTransactions(fromAccountingDate, toAccountingDate, historicised).getPayload());
    }

    @GetMapping(path = "/account/balance")
    public ResponseEntity<BalancePayloadDto> retreiveAccountBalance() throws URISyntaxException, JsonProcessingException {
        return ResponseEntity.ok(fabrickCommand.retrieveBalance().getPayload());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/account/moneyTransfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> executeMoneyTransfer(@RequestBody(required = true) ExecuteMoneyTransferRequestDto requestDto) throws URISyntaxException, JsonProcessingException {
        fabrickCommand.executeMoneyTransfer(requestDto);
        return ResponseEntity.ok("Bonifico effettuato con successo");
    }

}
