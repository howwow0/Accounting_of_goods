package org.how_wow.application.services;

import org.how_wow.application.dto.stockOperations.request.FilterStockOperationsRequest;
import org.how_wow.application.dto.stockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.exceptions.StockOperationsNotFoundException;


/**
 * Сервис для управления операциями со складом.
 * <p>
 * Предоставляет методы для создания, удаления и поиска операций пополнения и списания товаров.
 * </p>
 */
public interface StockOperationService {

    /**
     * Удаляет все операции по товару.
     * <p>
     * Этот метод удаляет все операции пополнения или списания для указанного товара
     * и устанавливает его количество в 0.
     * </p>
     *
     * @param goodsId ID товара, для которого нужно удалить все операции.
     */
    void deleteAllStocksByGoodsId(Long goodsId);

    /**
     * Создает новую операцию на складе.
     * <p>
     * Этот метод сохраняет операцию пополнения или списания товара на склад,
     * обновляет количество товара в базе данных и возвращает ответ с информацией
     * о созданной операции.
     * </p>
     *
     * @param stockOperationsRequest Данные для создания операции (тип операции, количество товара).
     * @return Ответ с информацией о созданной операции.
     */
    StockOperationsResponse createStock(StockOperationsRequest stockOperationsRequest);

    /**
     * Получает список операций по товару с пагинацией.
     * <p>
     * Этот метод позволяет получить список операций пополнения и списания для указанного товара,
     * с возможностью пагинации (разбиение на страницы).
     * </p>
     *
     * @param goodsId    ID товара, для которого нужно получить операции.
     * @param pageNumber Номер страницы.
     * @param pageSize   Размер страницы.
     * @return Пагинированный список операций.
     */
    PaginatedResult<StockOperationsResponse> findStockByGoodsId(Long goodsId, Long pageNumber, Long pageSize);

    /**
     * Удаляет операцию по ее ID.
     * <p>
     * Этот метод удаляет конкретную операцию по ее уникальному ID,
     * а также отменяет изменения в количестве товара в базе данных.
     * </p>
     *
     * @param stockId ID операции, которую нужно удалить.
     * @throws StockOperationsNotFoundException Если операция с таким ID не найдена.
     */
    void deleteStockById(Long stockId);

    /**
     * Проверяет существование операций по товару.
     * <p>
     * Этот метод проверяет, существуют ли операции пополнения или списания для
     * указанного товара в базе данных.
     * </p>
     *
     * @param goodsId ID товара.
     * @return true, если операции существуют, иначе false.
     */
    boolean existsByGoodsId(Long goodsId);

    /**
     * Получает список операций по товару с фильтрацией по типу операции и диапазону дат.
     * <p>
     * Этот метод позволяет получить список операций пополнения и списания для указанного товара,
     * с возможностью фильтрации по типу операции и диапазону дат.
     * </p>
     *
     * @param goodsId                      ID товара, для которого нужно получить операции.
     * @param filterStockOperationsRequest Запрос с фильтрацией (тип операции, диапазон дат).
     * @param pageNumber                   Номер страницы.
     * @param pageSize                     Размер страницы.
     * @return Пагинированный список операций.
     */
    PaginatedResult<StockOperationsResponse> findStocksByGoodsIdAndOperationTypeAndTimeDatesWithPagination(
            Long goodsId,
            FilterStockOperationsRequest filterStockOperationsRequest,
            Long pageNumber,
            Long pageSize
    );
}
