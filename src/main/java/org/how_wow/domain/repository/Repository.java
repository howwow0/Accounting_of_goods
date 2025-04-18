package org.how_wow.domain.repository;

import org.how_wow.application.dto.repository.PaginatedResult;

import java.util.Optional;

/**
 * Базовый интерфейс для репозиториев, предоставляющий CRUD-операции (Create, Read, Update, Delete).
 *
 * @param <T>  Тип сущности, с которой работает репозиторий.
 * @param <ID> Тип уникального идентификатора сущности.
 */
public interface Repository<T, ID> {

    /**
     * Сохраняет сущность в репозитории. Если сущность уже существует, она обновляется.
     *
     * @param entity Сущность для сохранения или обновления.
     * @param <S>    Тип сущности.
     * @return Сохраненная или обновленная сущность.
     */
    <S extends T> S save(S entity);

    /**
     * Находит сущность по её уникальному идентификатору.
     *
     * @param id Уникальный идентификатор сущности.
     * @return {@link Optional}, содержащий сущность, если она найдена, или пустой {@link Optional}, если сущность не найдена.
     */
    Optional<T> findById(ID id);

    /**
     * Проверяет, существует ли сущность с указанным идентификатором.
     *
     * @param id Уникальный идентификатор сущности.
     * @return {@code true}, если сущность существует, иначе {@code false}.
     */
    boolean existsById(ID id);


    /**
     * Возвращает все сущности из репозитория с пагинацией.
     *
     * @param pageNumber Номер страницы.
     * @param pageSize   Размер страницы.
     * @return Результат поиска сущностей с пагинацией.
     */
    PaginatedResult<T> findAllWithPaging(long pageNumber, long pageSize);

    /**
     * Возвращает количество сущностей в репозитории.
     *
     * @return Количество сущностей.
     */
    long count();

    /**
     * Удаляет сущность по её уникальному идентификатору.
     *
     * @param id Уникальный идентификатор сущности для удаления.
     */
    void deleteById(ID id);


}