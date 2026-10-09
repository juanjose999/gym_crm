package com.gymAdmin.common.api;

/**
 * Envoltorio estándar para todas las respuestas de la API.
 */
public record ApiResponse<T>(boolean success, String message, T data) {

    private static final String MENSAJE_EXITO = "Operación exitosa";

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, MENSAJE_EXITO, data);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
