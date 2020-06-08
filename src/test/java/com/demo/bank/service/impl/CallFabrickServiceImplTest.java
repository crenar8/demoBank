package com.demo.bank.service.impl;

import com.demo.bank.dto.AccountBalanceResponseDto;
import com.demo.bank.dto.BalancePayloadDto;
import com.demo.bank.dto.ErrorDto;
import com.demo.bank.dto.ErrorResponseDto;
import com.demo.bank.exception.DemoException;
import com.demo.bank.service.CallFabrickService;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CallFabrickServiceImplTest {

    @InjectMocks
    private CallFabrickServiceImpl callFabrickService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(callFabrickService, "fabrickAuthSchema", "S2S");
        ReflectionTestUtils.setField(callFabrickService, "fabrickApiKey", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP");
        ReflectionTestUtils.setField(callFabrickService, "fabrickBaseUrl", "https://sandbox.platfr.io");
        ReflectionTestUtils.setField(callFabrickService, "accountId", 1234567L);
    }

    @Test
    public void get() throws JsonProcessingException, URISyntaxException {
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).thenReturn(ResponseEntity.ok(new ObjectMapper().writeValueAsString(getAccountBalanceResponseDto())));
        Assert.assertNotNull(callFabrickService.get("fakeEndpoint"));
    }

    @Test
    public void post() throws URISyntaxException, JsonProcessingException {
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class))).thenReturn(ResponseEntity.ok(new ObjectMapper().writeValueAsString(getAccountBalanceResponseDto())));
        callFabrickService.post("fakeEndpoint", null);
    }

    @Test
    public void postExpectedError() throws URISyntaxException, JsonProcessingException {
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class))).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        when(objectMapper.readValue(anyString(), eq(ErrorResponseDto.class))).thenReturn(mockErrorResponseDto());
        callFabrickService.post("fakeEndpoint", null);
    }


    @Test(expected = HttpClientErrorException.class)
    public void postUnexpectedError() throws URISyntaxException, JsonProcessingException {
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class))).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        ErrorResponseDto errorResponseDto = mockErrorResponseDto();
        errorResponseDto.getErrors().get(0).setDescription("Unexpected error");
        errorResponseDto.getErrors().get(0).setCode("X");
        when(objectMapper.readValue(anyString(), eq(ErrorResponseDto.class))).thenReturn(errorResponseDto);
        callFabrickService.post("fakeEndpoint", null);
    }

    private ErrorResponseDto mockErrorResponseDto() {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus("KO");
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode("API000");
        errorDto.setDescription("Expected error");
        errorResponseDto.setErrors(Lists.newArrayList(errorDto));
        return errorResponseDto;
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
}