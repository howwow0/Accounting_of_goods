package org.how_wow.application.services;

import org.how_wow.application.dto.goods.request.FilterGoodsRequest;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.request.UpdateGoodsQuantityRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.exceptions.*;

/**
 * Сервис для управления товарами.
 * <p>
 * Предоставляет методы для создания, обновления, удаления и получения товаров, а также для работы
 * с количеством товаров на складе.
 * </p>
 */
public interface GoodsService {

    /**
     * Создает новый товар.
     *
     * @param goodsRequest данные нового товара
     * @return {@link GoodsResponse} с информацией о созданном товаре
     * @throws ValidationException если валидация данных не пройдена
     */
    GoodsResponse createGoods(GoodsRequest goodsRequest);

    /**
     * Получает список товаров с пагинацией.
     *
     * @param pageNumber номер страницы
     * @param pageSize размер страницы
     * @return {@link PaginatedResult} список товаров
     */
    PaginatedResult<GoodsResponse> getGoods(Long pageNumber, Long pageSize);

    /**
     * Получает список товаров с пагинацией и фильтрацией.
     *
     * @param pageNumber номер страницы
     * @param pageSize размер страницы
     * @param filterRequest фильтр для товаров
     * @return {@link PaginatedResult} список товаров
     */
    PaginatedResult<GoodsResponse> getGoods(Long pageNumber, Long pageSize, FilterGoodsRequest filterRequest);

    /**
     * Обновляет товар по ID.
     *
     * @param goodsId ID товара
     * @param goodsRequest новые данные товара
     * @return {@link GoodsResponse} с обновленной информацией о товаре
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse updateGoods(Long goodsId, GoodsRequest goodsRequest);

    /**
     * Удаляет товар по ID.
     *
     * @param goodsId ID товара
     * @throws GoodsNotFoundException если товар не найден
     * @throws GoodsHasOperationsException если товар имеет связанные операции
     */
    void deleteGoods(Long goodsId);

    /**
     * Получает товар по его ID.
     *
     * @param goodsId ID товара
     * @return {@link GoodsResponse} информация о товаре
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse getGoodsById(Long goodsId);

    /**
     * Обнуляет количество товара.
     *
     * @param goodsId ID товара
     * @return {@link GoodsResponse} с товаром с обнуленным количеством
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse setZeroQuantity(Long goodsId);

    /**
     * Проверяет существование товара по ID.
     *
     * @param goodsId ID товара
     * @return true, если товар существует, иначе false
     */
    boolean existsById(Long goodsId);

    /**
     * Обновляет количество товара.
     *
     * @param updateGoodsQuantityRequest запрос с данными для обновления количества
     * @return {@link GoodsResponse} с обновленным количеством товара
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse updateGoodsQuantity(UpdateGoodsQuantityRequest updateGoodsQuantityRequest);

    /**
     * Отменяет изменение количества товара.
     *
     * @param updateGoodsQuantityRequest запрос с данными для отмены операции
     * @return {@link GoodsResponse} с отмененной операцией
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse undoGoodsQuantity(UpdateGoodsQuantityRequest updateGoodsQuantityRequest);
}
