package org.example.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Goods {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
}
