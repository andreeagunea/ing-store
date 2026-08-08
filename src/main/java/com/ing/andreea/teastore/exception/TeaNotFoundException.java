package com.ing.andreea.teastore.exception;

public class TeaNotFoundException extends RuntimeException {

    public TeaNotFoundException(String message) {
        super(message);
    }
}