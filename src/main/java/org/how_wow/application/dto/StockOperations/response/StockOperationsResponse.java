package org.how_wow.application.dto.StockOperations.response;

import lombok.Builder;
import org.how_wow.domain.enums.OperationType;

import java.time.LocalDateTime;


/**
 * Ответ на запрос операций пополнения и списания товаров
 */
@Builder
public record StockOperationsResponse(Long id, Long goodsId, OperationType operationType, Long quantity,
                                      LocalDateTime operationDateTime) {
}
