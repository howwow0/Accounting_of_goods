package org.how_wow.exceptions;

/**
 * Исключение, выбрасываемое при попытке удалить товар, на который есть операции.
 * <p>
 * Это может произойти, если товар имеет связанные операции пополнения или списания,
 * и их нельзя удалить, пока эти операции существуют.
 * </p>
 */
public class GoodsHasOperationsException extends RuntimeException {
    public GoodsHasOperationsException(String message) {
        super(message);
    }
}
