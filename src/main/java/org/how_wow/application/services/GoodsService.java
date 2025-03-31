package org.how_wow.application.services;

import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.request.UpdateGoodsQuantityRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.exceptions.*;

/**
 * Сервис для управления товарами.
 */
public interface GoodsService {

    /**
     * Создает новый товар.
     * @param goodsRequest данные нового товара
     * @return созданный товар
     * @throws ValidationException если валидация не пройдена
     */
    GoodsResponse createGoods(GoodsRequest goodsRequest);

    /**
     * Получает список товаров с пагинацией.
     * @param pageNumber номер страницы
     * @param pageSize размер страницы
     * @return список товаров
     */
    PaginatedResult<GoodsResponse> getGoods(Long pageNumber, Long pageSize);

    /**
     * Обновляет товар по его ID.
     * @param goodsId ID товара
     * @param goodsRequest обновленные данные
     * @return обновленный товар
     * @throws ValidationException если валидация не пройдена
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse updateGoods(Long goodsId, GoodsRequest goodsRequest);

    /**
     * Удаляет товар по ID.
     * @param goodsId ID товара
     * @throws ValidationException если валидация не пройдена
     * @throws GoodsNotFoundException если товар не найден
     * @throws GoodsHasOperationsException если у товара есть связанные операции
     */
    void deleteGoods(Long goodsId);

    /**
     * Получает товар по его ID.
     * @param goodsId ID товара
     * @return найденный товар
     * @throws ValidationException если валидация не пройдена
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse getGoodsById(Long goodsId);

    /**
     * Обнуляет количество товара.
     * @param goodsId ID товара
     * @return обновленный товар с нулевым количеством
     * @throws ValidationException если валидация не пройдена
     * @throws GoodsNotFoundException если товар не найден
     */
    GoodsResponse setZeroQuantity(Long goodsId);

    /**
     * Проверяет существование товара по ID.
     * @param goodsId ID товара
     * @return true, если товар существует
     * @throws ValidationException если валидация не пройдена
     */
    boolean existsById(Long goodsId);

    /**
     * Обновляет количество товара.
     * @param updateGoodsQuantityRequest данные для обновления количества
     * @return обновленный товар
     * @throws ValidationException если валидация не пройдена
     * @throws GoodsNotFoundException если товар не найден
     * @throws InsufficientGoodsException если товара на складе недостаточно, при выгрузке
     */
    GoodsResponse updateGoodsQuantity(UpdateGoodsQuantityRequest updateGoodsQuantityRequest);

    /**
     * Отменяет изменение количества товара.
     * @param updateGoodsQuantityRequest данные для отмены
     * @return обновленный товар
     * @throws ValidationException если валидация не пройдена
     * @throws GoodsNotFoundException если товар не найден
     * @throws UndoOperationException если текущее количество на складе, меньше отменяемого
     */
    GoodsResponse undoGoodsQuantity(UpdateGoodsQuantityRequest updateGoodsQuantityRequest);
}