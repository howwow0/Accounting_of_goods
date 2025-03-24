package org.how_wow.exceptions;

public class StockOperationsNotFoundException extends RuntimeException {
    public StockOperationsNotFoundException(String message) {
        super(message);
    }
}
