package org.how_wow.application.dto.StockOperations.request;

import lombok.Builder;
import org.how_wow.domain.enums.OperationType;

/**
 * Запрос на операции пополнения и списания товаров
 */
@Builder
public record StockOperationsRequest(Long goodsId, OperationType operationType, Long quantity) {
}
