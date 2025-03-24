package org.how_wow.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.how_wow.domain.enums.OperationType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class StockOperations {
    private Long id;
    private Long goodsId;
    private OperationType operationType;
    private Long quantity;
    private LocalDateTime operationDateTime;
}
