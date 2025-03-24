package org.how_wow.application.mappers;

import org.how_wow.application.dto.StockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.StockOperations.response.StockOperationsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.model.StockOperations;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Маппер для операций со складом
 */
public class StockOperationsMapper {

    /**
     * Преобразование операции в ответ
     *
     * @param stockOperations операция
     * @return ответ
     */
    public StockOperationsResponse toStockOperationsResponse(StockOperations stockOperations) {
        return StockOperationsResponse.builder()
                .id(stockOperations.getId())
                .operationType(stockOperations.getOperationType())
                .goodsId(stockOperations.getGoodsId())
                .quantity(stockOperations.getQuantity())
                .operationDateTime(stockOperations.getOperationDateTime())
                .build();
    }

    /**
     * Преобразование запроса в операцию
     *
     * @param stockOperationsRequest запрос
     * @return операция
     */
    public StockOperations toStockOperations(StockOperationsRequest stockOperationsRequest) {
        return StockOperations.builder()
                .operationType(stockOperationsRequest.operationType())
                .goodsId(stockOperationsRequest.goodsId())
                .quantity(stockOperationsRequest.quantity())
                .operationDateTime(LocalDateTime.now())
                .build();
    }

    /**
     * Преобразование пагинированного списка операций в ответ
     *
     * @param byGoodsIdWithPaging пагинированный список операций
     * @return пагинированный список ответов
     */
    public PaginatedResult<StockOperationsResponse> toPaginatedGoodsResponse(PaginatedResult<StockOperations> byGoodsIdWithPaging) {
        PaginatedResult<StockOperationsResponse> result = PaginatedResult.<StockOperationsResponse>builder()
                .pageSize(byGoodsIdWithPaging.getPageSize())
                .currentPage(byGoodsIdWithPaging.getCurrentPage())
                .totalItems(byGoodsIdWithPaging.getTotalItems())
                .totalPages(byGoodsIdWithPaging.getTotalPages())
                .build();
        result.setContent(byGoodsIdWithPaging.getContent().stream()
                .map(this::toStockOperationsResponse)
                .collect(Collectors.toList()));
        return result;
    }
}
