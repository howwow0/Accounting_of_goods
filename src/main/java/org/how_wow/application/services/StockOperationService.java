package org.how_wow.application.services;

import org.how_wow.application.dto.StockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.StockOperations.response.StockOperationsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;

/**
 * Сервис для работы с операциями пополнения или списания товаров
 */
public interface StockOperationService {
    /**
     * Удаление всех операций с товаром по идентификатору товара
     *
     * @param goodsId идентификатор товара
     */
    void deleteAllStocksByGoodsId(Long goodsId);

    /**
     * Создание операции пополнения или списания товаров
     *
     * @param stockOperationsRequest запрос на операцию
     * @return ответ на запрос
     */
    StockOperationsResponse createStock(StockOperationsRequest stockOperationsRequest);

    /**
     * Поиск операций пополнения или списания товаров по идентификатору товара
     *
     * @param goodsId идентификатор товара
     * @param pageNumber    номер страницы
     * @param pageSize    размер страницы
     * @return ответ на запрос
     */
    PaginatedResult<StockOperationsResponse> findStockByGoodsId(Long goodsId, Long pageNumber, Long pageSize);

    /**
     * Удаление операции пополнения или списания товаров по идентификатору остатков товаров
     *
     * @param stockId идентификатор остатков товаров
     */
    void deleteStockById(Long stockId);
}
