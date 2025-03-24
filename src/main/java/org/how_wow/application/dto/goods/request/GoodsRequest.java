package org.how_wow.application.dto.goods.request;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Запрос на создание товара
 */
@Builder
public record GoodsRequest(String name, String category, BigDecimal price) {
}
