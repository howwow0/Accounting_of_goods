package org.how_wow.application.dto.goods.request;

import lombok.Builder;
import org.how_wow.domain.enums.OperationType;

@Builder
public record UpdateGoodsQuantityRequest(Long goodsId, Long quantity, OperationType operationType) {
}
