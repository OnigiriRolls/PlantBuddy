package com.szi.plantbuddy.exception;

public class ModelException extends Exception {
    public ModelException(String message) {
        super(message);
    }

    public ModelException(Throwable cause) {
        super(cause);
    }
}
