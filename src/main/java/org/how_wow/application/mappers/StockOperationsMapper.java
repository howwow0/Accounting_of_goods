package org.how_wow.application.mappers;

import org.how_wow.application.dto.stockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.model.StockOperations;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Маппер для операций со складом.
 * Этот класс используется для преобразования между моделями операций и их представлениями,
 * такими как запросы и ответы.
 */
public class StockOperationsMapper {

    /**
     * Преобразование модели операции в ответ на запрос.
     *
     * @param stockOperations операция, которую нужно преобразовать.
     * @return объект {@link StockOperationsResponse} — ответ с информацией о операции.
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
     * Преобразование запроса на создание операции в модель операции.
     *
     * @param stockOperationsRequest запрос на создание операции.
     * @return объект {@link StockOperations} — модель операции, готовая для сохранения.
     */
    public StockOperations toStockOperations(StockOperationsRequest stockOperationsRequest) {
        return StockOperations.builder()
                .operationType(stockOperationsRequest.operationType())
                .goodsId(stockOperationsRequest.goodsId())
                .quantity(stockOperationsRequest.quantity())
                .operationDateTime(LocalDateTime.now())  // Время операции устанавливается на момент создания.
                .build();
    }

    /**
     * Преобразование пагинированного списка операций в пагинированный список ответов.
     *
     * @param byGoodsIdWithPaging пагинированный список операций.
     * @return объект {@link PaginatedResult} с преобразованными операциями в {@link StockOperationsResponse}.
     */
    public PaginatedResult<StockOperationsResponse> toPaginatedGoodsResponse(PaginatedResult<StockOperations> byGoodsIdWithPaging) {
        PaginatedResult<StockOperationsResponse> result = PaginatedResult.<StockOperationsResponse>builder()
                .pageSize(byGoodsIdWithPaging.getPageSize())
                .currentPage(byGoodsIdWithPaging.getCurrentPage())
                .totalItems(byGoodsIdWithPaging.getTotalItems())
                .totalPages(byGoodsIdWithPaging.getTotalPages())
                .build();
        result.setContent(byGoodsIdWithPaging.getContent().stream()
                .map(this::toStockOperationsResponse)  // Преобразуем каждую операцию в StockOperationsResponse
                .collect(Collectors.toList()));
        return result;
    }
}
