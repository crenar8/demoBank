package com.demo.bank.service.impl;

import com.demo.bank.dto.ErrorDto;
import com.demo.bank.dto.ErrorResponseDto;
import com.demo.bank.exception.DemoException;
import com.demo.bank.service.CallFabrickService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CallFabrickServiceImpl implements CallFabrickService {

    private static final Set<String> EXPECTED_ERROR_CODES = Sets.newHashSet("API000");

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${fabrick.api.key}")
    private String fabrickApiKey;

    @Value("${fabrick.auth.schema}")
    private String fabrickAuthSchema;

    @Value("${fabrick.base.url}")
    private String fabrickBaseUrl;

    @Value("${fabrick.account.id}")
    private Long accountId;

    @Override
    public ResponseEntity<String> get(String endpoint) throws URISyntaxException {
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, buildFabrickHeaders());
        ResponseEntity<String> response = restTemplate.exchange(new URI(fabrickBaseUrl.concat(endpoint)), HttpMethod.GET, requestEntity, String.class);
        checkResponse(response);
        return response;
    }

    @Override
    public void post(String endpoint, Object body) throws URISyntaxException, JsonProcessingException {
        boolean errorCatched = false;
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, buildFabrickHeaders());
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(new URI(fabrickBaseUrl.concat(endpoint)), HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            errorCatched = true;
            if (!isAnExpectedError(e.getResponseBodyAsString())) {
                throw e;
            }
        }
        if (!errorCatched) {
            checkResponse(response);
        }
    }

    private void checkResponse(ResponseEntity<String> response) {
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            String body = response.getBody();
            String errorMessage = "Errore generico";
            try {
                errorMessage = ((ErrorDto) Iterables.getFirst(objectMapper.readValue(body, ErrorResponseDto.class).getErrors(), new ErrorResponseDto())).getDescription();
            } catch (Exception ignored) { }
            throw new DemoException(errorMessage);
        }
    }

    private boolean isAnExpectedError(String body) {
        boolean result = false;
        try {
            ErrorDto errorDto = (ErrorDto) Iterables.getFirst(objectMapper.readValue(body, ErrorResponseDto.class).getErrors(), new ErrorResponseDto());
            result = errorDto != null && EXPECTED_ERROR_CODES.contains(errorDto.getCode());
        } catch (JsonProcessingException ignored) {
        }
        return result;
    }

    private HttpHeaders buildFabrickHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Auth-Schema", fabrickAuthSchema);
        headers.set("Api-Key", fabrickApiKey);
        return headers;
    }
}
