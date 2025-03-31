package org.how_wow.application.services;

import org.how_wow.application.dto.StockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.StockOperations.response.StockOperationsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.exceptions.StockOperationsNotFoundException;

/**
 * Сервис для управления операциями со складом.
 */
public interface StockOperationService {

    /**
     * Удаляет все операции по товару.
     * @param goodsId ID товара
     */
    void deleteAllStocksByGoodsId(Long goodsId);

    /**
     * Создает новую операцию на складе.
     * @param stockOperationsRequest данные операции
     * @return созданная операция
     */
    StockOperationsResponse createStock(StockOperationsRequest stockOperationsRequest);

    /**
     * Получает список операций по товару с пагинацией.
     * @param goodsId ID товара
     * @param pageNumber номер страницы
     * @param pageSize размер страницы
     * @return список операций
     */
    PaginatedResult<StockOperationsResponse> findStockByGoodsId(Long goodsId, Long pageNumber, Long pageSize);

    /**
     * Удаляет операцию по ее ID.
     * @param stockId ID операции
     * @throws StockOperationsNotFoundException если операция не найдена
     */
    void deleteStockById(Long stockId);

    /**
     * Проверяет существование операций по товару.
     * @param goodsId ID товара
     * @return true, если операции существуют
     */
    boolean existsByGoodsId(Long goodsId);
}
