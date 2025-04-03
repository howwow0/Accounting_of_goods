package org.how_wow.infrastructure.persistense;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.model.Goods;
import org.how_wow.domain.repository.GoodsRepository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация репозитория товаров в памяти.
 * Хранит товары в коллекции и предоставляет базовые операции CRUD, а также пагинацию.
 */
@RequiredArgsConstructor
public class InMemoryGoodsRepository implements GoodsRepository {
    private final Map<Long, Goods> goodsMap;
    private long currentId = 0L;

    /**
     * Сохраняет новый или обновленный товар в репозитории.
     *
     * @param entity товар для сохранения
     * @param <S> тип товара
     * @return сохраненный товар
     */
    @Override
    public <S extends Goods> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(++currentId);
        }
        goodsMap.put(entity.getId(), entity);
        return entity;
    }

    /**
     * Находит товар по его ID.
     *
     * @param aLong ID товара
     * @return товар, если он существует, иначе Optional.empty()
     */
    @Override
    public Optional<Goods> findById(Long aLong) {
        return Optional.ofNullable(goodsMap.get(aLong));
    }

    /**
     * Проверяет существование товара по его ID.
     *
     * @param aLong ID товара
     * @return true, если товар существует, иначе false
     */
    @Override
    public boolean existsById(Long aLong) {
        return goodsMap.containsKey(aLong);
    }

    /**
     * Находит все товары с пагинацией.
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return пагинированный список товаров
     */
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

    /**
     * Возвращает общее количество товаров в репозитории.
     *
     * @return количество товаров
     */
    @Override
    public long count() {
        return goodsMap.size();
    }

    /**
     * Удаляет товар по его ID.
     *
     * @param aLong ID товара
     */
    @Override
    public void deleteById(Long aLong) {
        goodsMap.remove(aLong);
    }

    /**
     * Находит товары по их названию и категории с пагинацией.
     * Товары фильтруются по подстрочному совпадению в имени и категории.
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @param name       название товара (частичное совпадение)
     * @param category   категория товара (частичное совпадение)
     * @return пагинированный результат поиска товаров
     */
    @Override
    public PaginatedResult<Goods> findContainsByNameAndCategoryWithPaging(long pageNumber, long pageSize, String name, String category) {
        return PaginatedResult.<Goods>builder()
                .currentPage(pageNumber)
                .totalPages(goodsMap.size() / pageSize)
                .pageSize(pageSize)
                .totalItems((long) goodsMap.size())
                .content(goodsMap.values().stream()
                        .filter(goods -> goods.getName().contains(name) && goods.getCategory().contains(category))
                        .skip((pageNumber - 1) * pageSize)
                        .limit(pageSize)
                        .collect(Collectors.toList()))
                .build();
    }
}
