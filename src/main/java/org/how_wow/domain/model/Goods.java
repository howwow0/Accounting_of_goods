package org.how_wow.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class Goods {
    private Long id;
    private String name;
    private String category;
    private Long quantity;
    private BigDecimal price;

    public BigDecimal getTotalCost() {
        return price.multiply(new BigDecimal(quantity));
    }
}
