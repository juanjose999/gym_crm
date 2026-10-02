package com.gymAdmin.exception;

import com.gymAdmin.controller.ResponseCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones cuando un recurso no es encontrado (HTTP 404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseCustom> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseCustom.error(ex.getMessage()));
    }

    /**
     * Maneja los errores de validación de los DTOs con @Valid (HTTP 400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseCustom> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Extrae el mensaje específico del primer error de validación encontrado
        FieldError fieldError = (FieldError) ex.getBindingResult().getAllErrors().get(0);
        String mensaje = fieldError.getField() + ": " + fieldError.getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseCustom.error(mensaje));
    }

    /**
     * Maneja argumentos ilegales o reglas de negocio violadas (HTTP 400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseCustom> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseCustom.error(ex.getMessage()));
    }

    /**
     * Manejador genérico para cualquier otro error inesperado (HTTP 500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseCustom> handleGeneralException(Exception ex) {
        // En producción podrías loguear el error real: ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseCustom.error("Ocurrió un error interno en el servidor: " + ex.getMessage()));
    }
}