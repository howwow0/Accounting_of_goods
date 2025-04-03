package org.how_wow.exceptions;

/**
 * Исключение, выбрасываемое при недостаточном количестве товара.
 * <p>
 * Это может произойти, если операция пополнения или списания товара
 * не может быть выполнена из-за недостатка товара на складе.
 * </p>
 */
public class InsufficientGoodsException extends RuntimeException {
    public InsufficientGoodsException(String message) {
        super(message);
    }
}
