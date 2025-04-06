package org.how_wow.application.dto.stockOperations.request;

import lombok.Builder;
import org.how_wow.domain.enums.OperationType;

import java.time.LocalDateTime;

/**
 * Запрос для фильтрации операций со складом.
 * <p>
 * Этот класс используется для передачи параметров фильтрации операций
 * по типу операции и диапазону дат.
 * </p>
 *
 * @param operationType Тип операции (пополнение или списание).
 * @param startDateTime Дата и время начала диапазона.
 * @param endDateTime   Дата и время окончания диапазона.
 */
@Builder
public record FilterStockOperationsRequest(OperationType operationType,
                                           LocalDateTime startDateTime,
                                           LocalDateTime endDateTime) {
}
