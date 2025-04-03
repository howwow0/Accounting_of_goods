package org.how_wow.application.dto.StockOperations.response;

import lombok.Builder;
import org.how_wow.domain.enums.OperationType;
import java.time.LocalDateTime;

/**
 * Ответ на запрос операций пополнения и списания товаров на складе.
 *
 * @param id               Идентификатор операции.
 * @param goodsId          Идентификатор товара, с которым была выполнена операция.
 * @param operationType    Тип операции (например, {@link OperationType#INBOUND} для пополнения или {@link OperationType#OUTBOUND} для списания).
 * @param quantity         Количество товара, которое было добавлено или списано.
 * @param operationDateTime Дата и время выполнения операции.
 */
@Builder
public record StockOperationsResponse(Long id, Long goodsId, OperationType operationType, Long quantity,
                                      LocalDateTime operationDateTime) {
}
