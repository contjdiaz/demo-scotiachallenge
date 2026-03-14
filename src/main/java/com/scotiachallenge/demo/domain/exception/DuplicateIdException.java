package com.scotiachallenge.demo.domain.exception;

public class DuplicateIdException  extends RuntimeException {
    public DuplicateIdException(String message) {
        super(message);
    }
    
}
