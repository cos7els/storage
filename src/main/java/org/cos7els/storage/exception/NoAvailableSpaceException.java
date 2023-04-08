package org.cos7els.storage.exception;

import lombok.Data;

@Data
public class NoAvailableSpaceException extends RuntimeException {
    public NoAvailableSpaceException(String message) {
        super(message);
    }
}
