package org.how_wow.application.dto.goods.request;

import lombok.Builder;

/**
 * DTO для передачи параметров фильтрации товаров.
 *
 * @param name     Название товара для фильтрации (может быть частичным).
 * @param category Категория товара для фильтрации.
 */
@Builder
public record FilterGoodsRequest(String name, String category) {
}
