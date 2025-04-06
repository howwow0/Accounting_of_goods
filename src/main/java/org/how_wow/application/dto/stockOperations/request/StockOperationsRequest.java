package org.how_wow.application.dto.stockOperations.request;

import lombok.Builder;
import org.how_wow.domain.enums.OperationType;

/**
 * DTO-запрос для выполнения операций пополнения или списания товаров на складе.
 *
 * @param goodsId       Идентификатор товара, с которым будет выполняться операция.
 * @param operationType Тип операции (например, {@link OperationType#INBOUND} для пополнения или {@link OperationType#OUTBOUND} для списания).
 * @param quantity      Количество товара для выполнения операции (может быть как положительным, так и отрицательным в зависимости от операции).
 */
@Builder
public record StockOperationsRequest(Long goodsId, OperationType operationType, Long quantity) {
}
