package org.how_wow.domain.repository;

import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.domain.model.StockOperations;

import java.time.LocalDateTime;


/**
 * Интерфейс репозитория для работы с операциями со складом.
 * <p>
 * Предоставляет методы для выполнения операций CRUD (Create, Read, Update, Delete)
 * над сущностями операции со складом.
 * </p>
 */
public interface StockOperationsRepository extends Repository<StockOperations, Long> {
    /**
     * Найти операции по идентификатору товара с пагинацией по номеру страницы и размеру страницы
     *
     * @param goodsId    идентификатор товара
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return список операций
     */
    PaginatedResult<StockOperations> findByGoodsIdWithPaging(long goodsId, long pageNumber, long pageSize);

    /**
     * Удалить все операции по идентификатору товара
     *
     * @param goodsId идентификатор товара
     */
    void deleteAllByGoodsId(long goodsId);

    /**
     * Проверяет, существуют ли операции по идентификатору товара
     *
     * @param goodsId идентификатор товара
     * @return true, если операции существуют, иначе false
     */
    boolean existsByGoodsId(Long goodsId);

    /**
     * Найти операции по идентификатору товара, типу операции и диапазону дат с пагинацией
     *
     * @param goodsId       идентификатор товара
     * @param operationType тип операции
     * @param startDateTime начальная дата
     * @param endDateTime   конечная дата
     * @param pageNumber    номер страницы
     * @param pageSize      размер страницы
     * @return список операций
     */
    PaginatedResult<StockOperations> findContainsByGoodsIdAndOperationTypeAndBetweenTimeDatesWithPaging(
            long goodsId,
            OperationType operationType,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            long pageNumber,
            long pageSize
    );
}
