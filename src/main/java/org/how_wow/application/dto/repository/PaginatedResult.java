package org.how_wow.application.dto.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
 * Результат запроса с пагинацией
 */
@Builder
@Getter
@Setter
public class PaginatedResult<T> {
    private List<T> content;
    private Long currentPage;
    private Long pageSize;
    private Long totalPages;
    private Long totalItems;
}