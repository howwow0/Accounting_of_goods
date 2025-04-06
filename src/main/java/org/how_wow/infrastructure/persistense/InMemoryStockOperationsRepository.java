package org.how_wow.infrastructure.persistense;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.domain.model.StockOperations;
import org.how_wow.domain.repository.StockOperationsRepository;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация репозитория операций со складом в памяти.
 * Хранит операции в коллекции и предоставляет базовые операции CRUD, а также пагинацию.
 */
@RequiredArgsConstructor
public class InMemoryStockOperationsRepository implements StockOperationsRepository {
    private final Map<Long, StockOperations> stockOperations;
    private long currentId = 0L;

    /**
     * Находит операции по ID товара с пагинацией.
     *
     * @param goodsId    ID товара
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return пагинированный список операций
     */
    @Override
    public PaginatedResult<StockOperations> findByGoodsIdWithPaging(long goodsId, long pageNumber, long pageSize) {
        return PaginatedResult.<StockOperations>builder()
                .currentPage(pageNumber)
                .totalPages(stockOperations.size() / pageSize)
                .pageSize(pageSize)
                .totalItems((long) stockOperations.size())
                .content(stockOperations.values().stream()
                        .filter(stockOperation -> stockOperation.getGoodsId() == goodsId)
                        .skip((pageNumber - 1) * pageSize)
                        .limit(pageSize)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Удаляет все операции по ID товара.
     *
     * @param goodsId ID товара
     */
    @Override
    public void deleteAllByGoodsId(long goodsId) {
        Iterator<Map.Entry<Long, StockOperations>> iterator = stockOperations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, StockOperations> entry = iterator.next();
            StockOperations stockOperation = entry.getValue();
            if (stockOperation.getGoodsId() == goodsId) {
                iterator.remove();
            }
        }
    }

    /**
     * Проверяет существование операций по ID товара.
     *
     * @param goodsId ID товара
     * @return true, если операции существуют, иначе false
     */
    @Override
    public boolean existsByGoodsId(Long goodsId) {
        return stockOperations.values().stream()
                .anyMatch(stockOperation -> stockOperation.getGoodsId().equals(goodsId));
    }

    /**
     * Находит операции по ID товара, типу операции и диапазону дат с пагинацией.
     *
     * @param goodsId       ID товара
     * @param operationType тип операции
     * @param startDateTime начальная дата
     * @param endDateTime   конечная дата
     * @param pageNumber    номер страницы
     * @param pageSize      размер страницы
     * @return пагинированный список операций
     */
    @Override
    public PaginatedResult<StockOperations> findContainsByGoodsIdAndOperationTypeAndBetweenTimeDatesWithPaging(long goodsId, OperationType operationType, LocalDateTime startDateTime, LocalDateTime endDateTime, long pageNumber, long pageSize) {
        return PaginatedResult.<StockOperations>builder()
                .currentPage(pageNumber)
                .totalPages(stockOperations.size() / pageSize)
                .pageSize(pageSize)
                .totalItems((long) stockOperations.size())
                .content(stockOperations.values().stream()
                        .filter(stockOperation -> stockOperation.getGoodsId() == goodsId
                                && (operationType == null || stockOperation.getOperationType() == operationType)
                                && (stockOperation.getOperationDateTime().isAfter(startDateTime) || stockOperation.getOperationDateTime().isEqual(startDateTime))
                                && (stockOperation.getOperationDateTime().isBefore(endDateTime) || stockOperation.getOperationDateTime().isEqual(endDateTime)))
                        .skip((pageNumber - 1) * pageSize)
                        .limit(pageSize)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Сохраняет новую или обновленную операцию в репозитории.
     *
     * @param entity операция для сохранения
     * @param <S>    тип операции
     * @return сохраненная операция
     */
    @Override
    public <S extends StockOperations> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(++currentId);
        }
        stockOperations.put(entity.getId(), entity);
        return entity;
    }

    /**
     * Находит операцию по ее ID.
     *
     * @param aLong ID операции
     * @return операция, если она существует, иначе Optional.empty()
     */
    @Override
    public Optional<StockOperations> findById(Long aLong) {
        return Optional.ofNullable(stockOperations.get(aLong));
    }

    /**
     * Проверяет существование операции по ее ID.
     *
     * @param aLong ID операции
     * @return true, если операция существует, иначе false
     */
    @Override
    public boolean existsById(Long aLong) {
        return stockOperations.containsKey(aLong);
    }

    /**
     * Находит все операции с пагинацией.
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return пагинированный список операций
     */
    @Override
    public PaginatedResult<StockOperations> findAllWithPaging(long pageNumber, long pageSize) {
        return PaginatedResult.<StockOperations>builder()
                .currentPage(pageNumber)
                .totalPages(stockOperations.size() / pageSize)
                .pageSize(pageSize)
                .totalItems((long) stockOperations.size())
                .content(stockOperations.values().stream()
                        .skip((pageNumber - 1) * pageSize)
                        .limit(pageSize)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Возвращает общее количество операций в репозитории.
     *
     * @return количество операций
     */
    @Override
    public long count() {
        return stockOperations.size();
    }

    /**
     * Удаляет операцию по ее ID.
     *
     * @param aLong ID операции
     */
    @Override
    public void deleteById(Long aLong) {
        stockOperations.remove(aLong);
    }
}
