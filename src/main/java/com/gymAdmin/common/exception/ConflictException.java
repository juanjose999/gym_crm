package com.gymAdmin.common.exception;

/**
 * La operación entra en conflicto con el estado actual de los datos, por ejemplo un duplicado (HTTP 409).
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
