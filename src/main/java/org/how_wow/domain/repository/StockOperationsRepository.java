package org.how_wow.domain.repository;

import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.model.StockOperations;


/*
 * Репозиторий для работы с операциями на складе
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

    boolean existsByGoodsId(Long goodsId);
}
