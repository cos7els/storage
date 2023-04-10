package org.cos7els.storage.exception;

public class NoAvailableSpaceException extends RuntimeException {
    public NoAvailableSpaceException(String message) {
        super(message);
    }
}