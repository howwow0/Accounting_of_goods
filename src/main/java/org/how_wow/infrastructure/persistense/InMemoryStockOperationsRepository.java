package org.how_wow.infrastructure.persistense;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.model.StockOperations;
import org.how_wow.domain.repository.StockOperationsRepository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация репозитория операций со складом в памяти
 */
@RequiredArgsConstructor
public class InMemoryStockOperationsRepository implements StockOperationsRepository {
    private final Map<Long, StockOperations> stockOperations;
    private long currentId = 0L;

    @Override
    public PaginatedResult<StockOperations> findByGoodsIdWithPaging(long goodsId, long pageNumber, long pageSize) {
        return PaginatedResult.<StockOperations>builder()
                .currentPage(pageNumber)
                .totalPages(stockOperations.size() / pageSize)
                .pageSize(pageSize)
                .totalItems((long) stockOperations.size())
                .content(stockOperations.values().stream()
                        .filter(stockOperation -> stockOperation.getGoodsId() == goodsId)
                        .skip(pageNumber * pageSize)
                        .limit(pageSize)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void deleteAllByGoodsId(long goodsId) {
        for (StockOperations stockOperation : stockOperations.values()) {
            if (stockOperation.getGoodsId() == goodsId) {
                stockOperations.remove(stockOperation.getId());
            }
        }
    }

    @Override
    public boolean existsByGoodsId(Long goodsId) {
        return stockOperations.values().stream()
                .anyMatch(stockOperation -> stockOperation.getGoodsId().equals(goodsId));
    }

    @Override
    public <S extends StockOperations> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(++currentId);
        }
        stockOperations.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<StockOperations> findById(Long aLong) {
        return Optional.ofNullable(stockOperations.get(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        return stockOperations.containsKey(aLong);
    }

    @Override
    public PaginatedResult<StockOperations> findAllWithPaging(long pageNumber, long pageSize) {
        return PaginatedResult.<StockOperations>builder()
                .currentPage(pageNumber)
                .totalPages(stockOperations.size() / pageSize)
                .pageSize(pageSize)
                .totalItems((long) stockOperations.size())
                .content(stockOperations.values().stream()
                        .skip(pageNumber * pageSize)
                        .limit(pageSize)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public long count() {
        return stockOperations.size();
    }

    @Override
    public void deleteById(Long aLong) {
        stockOperations.remove(aLong);
    }
}
