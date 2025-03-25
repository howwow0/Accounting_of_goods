package org.how_wow.application.mappers;

import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.domain.model.Goods;
import org.how_wow.domain.model.StockOperations;

import java.util.stream.Collectors;

/**
 * Маппер для товаров
 */
public class GoodsMapper {

    /**
     * Преобразование товара в ответ
     *
     * @param goods товар
     * @return ответ
     */
    public GoodsResponse toGoodsResponse(Goods goods) {
        return GoodsResponse.builder()
                .id(goods.getId())
                .name(goods.getName())
                .price(goods.getPrice())
                .category(goods.getCategory())
                .totalCost(goods.getTotalCost())
                .build();
    }

    /**
     * Преобразование запроса в товар
     *
     * @param goodsRequest запрос
     * @return товар
     */
    public Goods toGoods(GoodsRequest goodsRequest) {
        return Goods.builder()
                .name(goodsRequest.name())
                .price(goodsRequest.price())
                .category(goodsRequest.category())
                .quantity(0L)
                .build();
    }

    /**
     * Обновление товара
     *
     * @param goods товар
     * @param goodsRequest запрос
     * @return обновленный товар
     */
    public Goods updateGoods(Goods goods, GoodsRequest goodsRequest) {
        return Goods.builder()
                .id(goods.getId())
                .name(goodsRequest.name() != null ? goodsRequest.name(): goods.getName())
                .price(goodsRequest.price() != null ? goodsRequest.price(): goods.getPrice())
                .category(goodsRequest.category() != null ? goodsRequest.category(): goods.getCategory())
                .build();
    }


    /**
     * Преобразование пагинированного списка товаров в пагинированный список ответов
     *
     * @param allWithPaging пагинированный список товаров
     * @return пагинированный список ответов
     */
    public PaginatedResult<GoodsResponse> toPaginatedGoodsResponse(PaginatedResult<Goods> allWithPaging) {
        PaginatedResult<GoodsResponse> result = PaginatedResult.<GoodsResponse>builder()
                .pageSize(allWithPaging.getPageSize())
                .currentPage(allWithPaging.getCurrentPage())
                .totalItems(allWithPaging.getTotalItems())
                .totalPages(allWithPaging.getTotalPages())
                .build();
        result.setContent(allWithPaging.getContent().stream()
                .map(this::toGoodsResponse)
                .collect(Collectors.toList()));
        return result;
    }

}
