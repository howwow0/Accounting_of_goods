package org.how_wow.exceptions;

/**
 * Исключение, выбрасываемое при отсутствии операции на складе.
 * <p>
 * Это может произойти, если операция не найдена в базе данных
 * или если операция не может быть выполнена.
 * </p>
 */
public class StockOperationsNotFoundException extends RuntimeException {
    public StockOperationsNotFoundException(String message) {
        super(message);
    }
}
