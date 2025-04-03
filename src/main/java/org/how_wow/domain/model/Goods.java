package org.how_wow.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Модель товара.
 * <p>
 * Содержит информацию о товаре, включая его ID, название, категорию,
 * количество и цену.
 * </p>
 */
@Builder
@Getter
@Setter
public class Goods {
    private Long id;
    private String name;
    private String category;
    private Long quantity;
    private BigDecimal price;

    /**
     * Метод для получения общей стоимости товара.
     * <p>
     * Рассчитывает общую стоимость товара, умножая цену на количество.
     * </p>
     *
     * @return Общая стоимость товара.
     */
    public BigDecimal getTotalCost() {
        return price.multiply(new BigDecimal(quantity));
    }
}
