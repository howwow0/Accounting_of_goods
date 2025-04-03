package org.how_wow.exceptions;

/**
 * Исключение, выбрасываемое при отсутствии товара.
 * <p>
 * Это может произойти, если товар не найден в базе данных
 * или если операция с ним не может быть выполнена.
 * </p>
 */
public class GoodsNotFoundException extends RuntimeException {
    public GoodsNotFoundException(String message) {
        super(message);
    }
}
