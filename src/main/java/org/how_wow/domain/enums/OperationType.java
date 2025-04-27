package org.how_wow.domain.enums;

/**
 * Перечисление типов операций со складом.
 * <p>
 * Содержит два типа операций: пополнение (INBOUND) и списание (OUTBOUND).
 * </p>
 */
public enum OperationType {
    INBOUND,
    OUTBOUND;

    public static OperationType fromString(String type) {
        return switch (type) {
            case "Поступление" -> INBOUND;
            case "Списание" -> OUTBOUND;
            default -> throw new IllegalArgumentException("Неизвестный тип операции: " + type);
        };
    }

    public static String toString(OperationType type) {
        return switch (type) {
            case INBOUND -> "Поступление";
            case OUTBOUND -> "Списание";
        };
    }
}
