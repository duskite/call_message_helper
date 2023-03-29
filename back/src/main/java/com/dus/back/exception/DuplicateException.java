package com.dus.back.exception;

public class DuplicateException extends RuntimeException{

    public DuplicateException(String message){
        super(message);
    }
}
