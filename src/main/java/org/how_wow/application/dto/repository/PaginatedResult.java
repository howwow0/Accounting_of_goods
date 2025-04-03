package org.how_wow.application.dto.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Обобщенный класс для представления пагинированного результата запроса.
 *
 * @param <T> Тип данных, содержащихся в списке контента.
 */
@Builder
@Getter
@Setter
public class PaginatedResult<T> {

    /**
     * Список элементов текущей страницы.
     */
    private List<T> content;

    /**
     * Текущая страница (начиная с 0 или 1, в зависимости от реализации).
     */
    private Long currentPage;

    /**
     * Количество элементов на одной странице.
     */
    private Long pageSize;

    /**
     * Общее количество страниц.
     */
    private Long totalPages;

    /**
     * Общее количество элементов во всей выборке.
     */
    private Long totalItems;
}
