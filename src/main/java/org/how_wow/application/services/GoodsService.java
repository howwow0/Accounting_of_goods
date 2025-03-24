package org.how_wow.application.services;

import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.exceptions.GoodsNotFoundException;
import org.how_wow.exceptions.ValidationException;

/**
 * Сервис для товаров
 */
public interface GoodsService {

    /**
     * Создание товара
     *
     * @param goodsRequest запрос на создание товара
     * @return ответ на создание товара
     * @throws ValidationException если запрос не прошел валидацию
     */
    GoodsResponse createGoods(GoodsRequest goodsRequest);

    /**
     * Получение списка товаров с пагинацией
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return список товаров с пагинацией
     * @throws ValidationException если номер страницы или размер страницы не прошли валидацию
     */
    PaginatedResult<GoodsResponse> getGoods(Long pageNumber, Long pageSize);

    /**
     * Обновление товара
     *
     * @param goodsId      идентификатор товара
     * @param goodsRequest запрос на обновление товара
     * @return ответ на обновление товара
     * @throws ValidationException    если запрос не прошел валидацию
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse updateGoods(Long goodsId, GoodsRequest goodsRequest);

    /**
     * Удаление товара
     *
     * @param goodsId идентификатор товара
     * @throws ValidationException    если идентификатор товара не прошел валидацию
     * @throws GoodsNotFoundException если товар не найден
     */
    void deleteGoods(Long goodsId);

    /**
     * Получение товара по идентификатору
     *
     * @param goodsId идентификатор товара
     * @return ответ на получение товара
     * @throws ValidationException    если идентификатор товара не прошел валидацию
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse getGoodsById(Long goodsId);
}
