package com.gymAdmin.common.exception;

/**
 * Se violó una regla de negocio (HTTP 400).
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
