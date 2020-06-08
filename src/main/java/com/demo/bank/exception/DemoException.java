package com.demo.bank.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DemoException extends RuntimeException{

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
