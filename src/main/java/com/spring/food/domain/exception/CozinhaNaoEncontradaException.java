package com.spring.food.domain.exception;

public class CozinhaNaoEncontradaException extends  RuntimeException{

    public CozinhaNaoEncontradaException(String message) {
        super(message);
    }

    public CozinhaNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
