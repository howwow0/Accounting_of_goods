package org.how_wow.exceptions;

/**
 * Исключение, выбрасываемое при ошибке валидации данных.
 * <p>
 * Это может произойти, если входные данные не соответствуют ожидаемым требованиям
 * или если есть проблемы с форматом данных.
 * </p>
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
