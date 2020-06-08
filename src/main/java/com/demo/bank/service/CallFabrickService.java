package com.demo.bank.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface CallFabrickService {

    ResponseEntity<String> get(String path) throws URISyntaxException;

    void post(String path, Object body) throws URISyntaxException, JsonProcessingException;
}
