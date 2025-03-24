package org.how_wow.application.dto.goods.response;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Ответ на запрос товара
 */
@Builder
public record GoodsResponse(Long id, String name, String category, long quantity, BigDecimal price, BigDecimal totalCost) {
}
