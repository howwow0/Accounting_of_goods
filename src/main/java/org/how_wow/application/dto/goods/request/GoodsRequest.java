package org.how_wow.application.dto.goods.request;

import lombok.Builder;
import java.math.BigDecimal;

/**
 * DTO-запрос для создания нового товара.
 *
 * @param name     Название товара.
 * @param category Категория товара.
 * @param price    Цена товара.
 */
@Builder
public record GoodsRequest(String name, String category, BigDecimal price) {
}
