package com.brandpromo.exception;

/**
 * Business rule violation exception.
 * Messages are safe to expose to the client (user-facing Chinese text).
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
