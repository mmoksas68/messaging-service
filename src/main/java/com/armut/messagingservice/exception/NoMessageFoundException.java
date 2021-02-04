package com.armut.messagingservice.exception;

public class NoMessageFoundException extends Exception {
    private String message = "Could not find a message with with the given information.";

    @Override
    public String getMessage() {
        return message;
    }
}
