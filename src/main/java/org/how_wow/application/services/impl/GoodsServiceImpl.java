package org.how_wow.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.mappers.GoodsMapper;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.application.validators.Validator;
import org.how_wow.domain.model.Goods;
import org.how_wow.domain.repository.GoodsRepository;
import org.how_wow.exceptions.GoodsNotFoundException;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {
    private final StockOperationService stockOperationService;
    private final GoodsRepository goodsRepository;
    private final GoodsMapper goodsMapper;
    private final Validator<Long> longIdValidator;
    private final Validator<String> nameValidator;
    private final Validator<String> categoryValidator;
    private final Validator<BigDecimal> priceValidator;
    private final Validator<Long> pageNumberAndPageSizeValidator;

    @Override
    public GoodsResponse createGoods(GoodsRequest goodsRequest) {
        log.info("Проверка валидности запроса на создание товара: {}", goodsRequest);
        nameValidator.validate(goodsRequest.name());
        categoryValidator.validate(goodsRequest.category());
        priceValidator.validate(goodsRequest.price());

        log.info("Создание товара: {}", goodsRequest);
        Goods goodsForSave = goodsMapper.toGoods(goodsRequest);
        Goods savedGoods = goodsRepository.save(goodsForSave);
        log.info("Товар создан с ID: {}", savedGoods.getId());
        return goodsMapper.toGoodsResponse(savedGoods);
    }

    @Override
    public PaginatedResult<GoodsResponse> getGoods(Long pageNumber, Long pageSize) {
        log.info("Проверка валидности номера страницы и размера страницы: pageNumber={}, pageSize={}", pageNumber, pageSize);
        pageNumberAndPageSizeValidator.validate(pageNumber);
        pageNumberAndPageSizeValidator.validate(pageSize);
        log.info("Получение списка товаров с пагинацией: pageNumber={}, pageSize={}", pageNumber, pageSize);
        return goodsMapper.toPaginatedGoodsResponse(goodsRepository.findAllWithPaging(pageNumber, pageSize));
    }

    @Override
    public GoodsResponse updateGoods(Long goodsId, GoodsRequest goodsRequest) {
        log.info("Проверка валидности запроса на обновление товара: {}", goodsRequest);
        longIdValidator.validate(goodsId);
        if (goodsRequest.name() != null)
            nameValidator.validate(goodsRequest.name());
        if (goodsRequest.category() != null)
            categoryValidator.validate(goodsRequest.category());
        if (goodsRequest.price() != null)
            priceValidator.validate(goodsRequest.price());

        log.info("Проверка существования товара с id {}", goodsId);
        Goods goodsForUpdate = goodsRepository.findById(goodsId)
                .orElseThrow(() -> {
                    log.error("Товар с ID {} не найден", goodsId);
                    return new GoodsNotFoundException("Товар с ID " + goodsId + " не найден");
                });

        log.info("Обновление товара с ID {}: {}", goodsId, goodsRequest);
        Goods updatedGoods = goodsRepository.save(
                goodsMapper.updateGoods(goodsForUpdate, goodsRequest)
        );

        log.info("Товар ID {} успешно обновлен", goodsId);
        return goodsMapper.toGoodsResponse(updatedGoods);
    }

    @Override
    public void deleteGoods(Long goodsId) {
        log.info("Проверка валидности ID товара для удаления: {}", goodsId);
        longIdValidator.validate(goodsId);
        if (!goodsRepository.existsById(goodsId))
            throw new GoodsNotFoundException("Товар с ID " + goodsId + " не найден");

        log.info("Удаление товара с ID {}", goodsId);
        goodsRepository.deleteById(goodsId);
        log.info("Удаление всех операций товара с ID {}", goodsId);
        stockOperationService.deleteAllStocksByGoodsId(goodsId);
        log.info("Товар ID {} успешно удален", goodsId);
    }

    @Override
    public GoodsResponse getGoodsById(Long goodsId) {
        log.info("Проверка валидности ID товара для получения: {}", goodsId);
        longIdValidator.validate(goodsId);
        log.info("Получение товара по ID {}", goodsId);
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> {
                    log.error("Товар с ID {} не найден", goodsId);
                    return new GoodsNotFoundException("Не удалось найти товар с ID: " + goodsId);
                });

        return goodsMapper.toGoodsResponse(goods);
    }
}
