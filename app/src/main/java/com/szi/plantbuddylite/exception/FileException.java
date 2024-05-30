package com.szi.plantbuddylite.exception;

public class FileException extends Exception {
    public static final String DIRECTORY_EXCEPTION_MESSAGE = "Pictures directory could not be created";
    public static final String JSON_EXCEPTION_MESSAGE = "Json could not be read";

    public FileException(String message) {
        super(message);
    }
}
