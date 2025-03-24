package org.example.infrastructure.persistense;

import lombok.RequiredArgsConstructor;
import org.example.domain.model.Goods;
import org.example.domain.repository.GoodsRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryGoodsRepository implements GoodsRepository {
    private Map<Long, Goods> goodsMap = new HashMap<>();
    private Long counter = 0L;

    @Override
    public <S extends Goods> S save(S entity) {
        return null;
    }

    @Override
    public Optional<Goods> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Goods> findAll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }
}
