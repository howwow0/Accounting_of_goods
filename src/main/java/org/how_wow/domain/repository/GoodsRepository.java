package org.how_wow.domain.repository;

import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.model.Goods;

/**
 * Интерфейс репозитория для работы с товарами.
 * <p>
 * Предоставляет методы для выполнения операций CRUD (Create, Read, Update, Delete)
 * над сущностями товаров.
 * </p>
 **/
public interface GoodsRepository extends Repository<Goods, Long> {
    /**
     * Находит товары по названию и категории с поддержкой пагинации.
     *
     * @param pageNumber Номер страницы (начинается с 1).
     * @param pageSize   Размер страницы (количество товаров на странице).
     * @param name       Часть названия товара для поиска.
     * @param category   Часть категории товара для поиска.
     * @return Пагинированный результат, содержащий товары, соответствующие критериям поиска по названию и категории.
     */
    PaginatedResult<Goods> findContainsByNameAndCategoryWithPaging(long pageNumber, long pageSize, String name, String category);

}
