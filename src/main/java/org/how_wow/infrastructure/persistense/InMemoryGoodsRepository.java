package org.how_wow.infrastructure.persistense;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.model.Goods;
import org.how_wow.domain.repository.GoodsRepository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация репозитория товаров в памяти
 */
@RequiredArgsConstructor
public class InMemoryGoodsRepository implements GoodsRepository {
    private final Map<Long, Goods> goodsMap;
    private long currentId = 0L;


    @Override
    public <S extends Goods> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(++currentId);
        }
        goodsMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Goods> findById(Long aLong) {
        return Optional.ofNullable(goodsMap.get(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        return goodsMap.containsKey(aLong);
    }

    @Override
    public PaginatedResult<Goods> findAllWithPaging(long pageNumber, long pageSize) {
        return PaginatedResult.<Goods>builder()
                .currentPage(pageNumber)
                .totalPages(goodsMap.size() / pageSize)
                .pageSize(pageSize)
                .totalItems((long) goodsMap.size())
                .content(goodsMap.values().stream()
                        .skip((pageNumber - 1) * pageSize)
                        .limit(pageSize)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public long count() {
        return goodsMap.size();
    }

    @Override
    public void deleteById(Long aLong) {
        goodsMap.remove(aLong);
    }
}
