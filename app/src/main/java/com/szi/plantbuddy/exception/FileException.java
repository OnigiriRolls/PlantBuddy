package com.szi.plantbuddy.exception;

public class FileException extends Exception {
    public static final String DIRECTORY_EXCEPTION_MESSAGE = "Pictures directory could not be created";

    public FileException(String message) {
        super(message);
    }
}
