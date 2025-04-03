package org.how_wow.exceptions;

/**
 * Исключение, выбрасываемое при ошибке отмены операции.
 * <p>
 * Это может произойти, если операция не найдена или не может быть отменена.
 * </p>
 */
public class UndoOperationException extends RuntimeException {
    public UndoOperationException(String message) {
        super(message);
    }
}
