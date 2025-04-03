package org.how_wow.application.mappers;

import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.domain.model.Goods;
import org.how_wow.exceptions.InsufficientGoodsException;
import org.how_wow.exceptions.UndoOperationException;

import java.util.stream.Collectors;

/**
 * Маппер для преобразования между моделями товаров и их представлениями.
 * Этот класс используется для конвертации данных между слоями приложения (например, между запросами и ответами).
 */
public class GoodsMapper {

    /**
     * Преобразование модели товара в ответ на запрос.
     *
     * @param goods модель товара.
     * @return объект {@link GoodsResponse} — ответ, содержащий информацию о товаре.
     */
    public GoodsResponse toGoodsResponse(Goods goods) {
        return GoodsResponse.builder()
                .id(goods.getId())
                .name(goods.getName())
                .price(goods.getPrice())
                .category(goods.getCategory())
                .totalCost(goods.getTotalCost())
                .quantity(goods.getQuantity())
                .build();
    }

    /**
     * Преобразование запроса на создание товара в модель товара.
     *
     * @param goodsRequest запрос на создание товара.
     * @return объект {@link Goods} — модель товара, готовая для сохранения.
     */
    public Goods toGoods(GoodsRequest goodsRequest) {
        return Goods.builder()
                .name(goodsRequest.name())
                .price(goodsRequest.price())
                .category(goodsRequest.category())
                .quantity(0L)  // Изначально количество товара равно 0
                .build();
    }

    /**
     * Обновление существующей модели товара на основе данных запроса.
     *
     * @param goods        существующий товар, который будет обновлен.
     * @param goodsRequest запрос с новыми данными товара.
     * @return обновленный объект {@link Goods}.
     */
    public Goods updateGoods(Goods goods, GoodsRequest goodsRequest) {
        return Goods.builder()
                .id(goods.getId())
                .name(goodsRequest.name())
                .price(goodsRequest.price())
                .category(goodsRequest.category())
                .quantity(goods.getQuantity())  // Количество остаётся неизменным
                .build();
    }

    /**
     * Преобразование пагинированного списка товаров в пагинированный список ответов.
     *
     * @param allWithPaging пагинированный список товаров.
     * @return объект {@link PaginatedResult} с преобразованными товарами в {@link GoodsResponse}.
     */
    public PaginatedResult<GoodsResponse> toPaginatedGoodsResponse(PaginatedResult<Goods> allWithPaging) {
        PaginatedResult<GoodsResponse> result = PaginatedResult.<GoodsResponse>builder()
                .pageSize(allWithPaging.getPageSize())
                .currentPage(allWithPaging.getCurrentPage())
                .totalItems(allWithPaging.getTotalItems())
                .totalPages(allWithPaging.getTotalPages())
                .build();
        result.setContent(allWithPaging.getContent().stream()
                .map(this::toGoodsResponse)  // Преобразуем каждый товар в GoodsResponse
                .collect(Collectors.toList()));
        return result;
    }

    /**
     * Обновление количества товара в зависимости от типа операции.
     * Операции могут быть как пополнением (INBOUND), так и списанием (OUTBOUND).
     *
     * @param goodsForUpdate  товар, количество которого нужно обновить.
     * @param quantity        количество товара для добавления или убавления.
     * @param operationType   тип операции (INBOUND/OUTBOUND).
     * @return обновленный объект {@link Goods}.
     * @throws InsufficientGoodsException если товара недостаточно для списания.
     */
    public Goods updateQuantity(Goods goodsForUpdate, Long quantity, OperationType operationType) {
        switch (operationType) {
            case INBOUND -> goodsForUpdate.setQuantity(goodsForUpdate.getQuantity() + quantity);
            case OUTBOUND -> {
                if (goodsForUpdate.getQuantity() < quantity) {
                    throw new InsufficientGoodsException(
                            "Недостаточно товара на складе. Доступно: " +
                                    goodsForUpdate.getQuantity() + ", требуется: " + quantity
                    );
                }
                goodsForUpdate.setQuantity(goodsForUpdate.getQuantity() - quantity);
            }
        }
        return goodsForUpdate;
    }

    /**
     * Отмена операции обновления количества товара.
     * Отмена может быть как для пополнения (INBOUND), так и для списания (OUTBOUND).
     *
     * @param goodsForUpdate  товар, количество которого нужно откатить.
     * @param quantity        количество товара для отката.
     * @param operationType   тип операции (INBOUND/OUTBOUND).
     * @return обновленный объект {@link Goods}.
     * @throws UndoOperationException если операция отмены невозможна (например, недостаточное количество для отмены пополнения).
     */
    public Goods undoQuantity(Goods goodsForUpdate, Long quantity, OperationType operationType) {
        switch (operationType) {
            case INBOUND -> {
                if (goodsForUpdate.getQuantity() < quantity) {
                    throw new UndoOperationException(
                            "Невозможно отменить приход: текущее количество меньше отменяемого"
                    );
                }
                goodsForUpdate.setQuantity(goodsForUpdate.getQuantity() - quantity);
            }
            case OUTBOUND -> goodsForUpdate.setQuantity(goodsForUpdate.getQuantity() + quantity);
        }
        return goodsForUpdate;
    }
}
