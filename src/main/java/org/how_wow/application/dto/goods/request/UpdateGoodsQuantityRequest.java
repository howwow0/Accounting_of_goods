package org.how_wow.application.dto.goods.request;

import lombok.Builder;
import org.how_wow.domain.enums.OperationType;

/**
 * DTO-запрос для обновления количества товара.
 *
 * @param goodsId       Идентификатор товара, количество которого необходимо изменить.
 * @param quantity      Количество для изменения (может быть положительным или отрицательным).
 * @param operationType Тип операции (например, {@link OperationType#INBOUND} или {@link OperationType#OUTBOUND}).
 */
@Builder
public record UpdateGoodsQuantityRequest(Long goodsId, Long quantity, OperationType operationType) {
}
