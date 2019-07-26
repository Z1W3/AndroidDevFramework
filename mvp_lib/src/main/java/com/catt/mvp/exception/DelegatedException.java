package com.catt.mvp.exception;

public class DelegatedException extends RuntimeException {

    private DelegatedException() {
    }

    public DelegatedException(String message) {
        super(message);
    }

    public DelegatedException(String message, Throwable cause) {
        super(message, cause);
    }

}
