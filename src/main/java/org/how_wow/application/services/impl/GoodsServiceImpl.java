package org.how_wow.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.request.UpdateGoodsQuantityRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.mappers.GoodsMapper;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.application.validators.Validator;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.domain.model.Goods;
import org.how_wow.domain.repository.GoodsRepository;
import org.how_wow.domain.repository.StockOperationsRepository;
import org.how_wow.exceptions.GoodsHasOperationsException;
import org.how_wow.exceptions.GoodsNotFoundException;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {
    private final StockOperationsRepository stockOperationsRepository;
    private final GoodsRepository goodsRepository;
    private final GoodsMapper goodsMapper;
    private final Validator<Long> longIdValidator;
    private final Validator<String> nameValidator;
    private final Validator<String> categoryValidator;
    private final Validator<BigDecimal> priceValidator;
    private final Validator<Long> pageNumberAndPageSizeValidator;
    private final Validator<Long> quantityValidator;
    private final Validator<OperationType> operationTypeValidator;

    @Override
    public GoodsResponse createGoods(GoodsRequest goodsRequest) {
        log.debug("Начало создания товара. Валидация данных...");
        nameValidator.validate(goodsRequest.name());
        categoryValidator.validate(goodsRequest.category());
        priceValidator.validate(goodsRequest.price());

        log.info("Создание нового товара: Наименование='{}', Категория='{}'",
                goodsRequest.name(), goodsRequest.category());
        Goods savedGoods = goodsRepository.save(goodsMapper.toGoods(goodsRequest));
        log.info("Успешно создан товар ID={}", savedGoods.getId());
        return goodsMapper.toGoodsResponse(savedGoods);
    }

    @Override
    public PaginatedResult<GoodsResponse> getGoods(Long pageNumber, Long pageSize) {
        log.debug("Получение товаров. Страница={}, Размер={}", pageNumber, pageSize);
        pageNumberAndPageSizeValidator.validate(pageNumber);
        pageNumberAndPageSizeValidator.validate(pageSize);

        return goodsMapper.toPaginatedGoodsResponse(goodsRepository.findAllWithPaging(pageNumber, pageSize));
    }

    @Override
    public GoodsResponse updateGoods(Long goodsId, GoodsRequest goodsRequest) {
        log.debug("Обновление товара ID={}", goodsId);
        longIdValidator.validate(goodsId);

        if (goodsRequest.name() != null) nameValidator.validate(goodsRequest.name());
        if (goodsRequest.category() != null) categoryValidator.validate(goodsRequest.category());
        if (goodsRequest.price() != null) priceValidator.validate(goodsRequest.price());

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> {
                    log.error("Товар не найден: ID={}", goodsId);
                    return new GoodsNotFoundException("Товар с ID " + goodsId + " не найден");
                });

        Goods updatedGoods = goodsRepository.save(goodsMapper.updateGoods(goods, goodsRequest));
        log.info("Товар ID={} успешно обновлен", goodsId);
        return goodsMapper.toGoodsResponse(updatedGoods);
    }

    @Override
    public void deleteGoods(Long goodsId) {
        log.debug("Попытка удаления товара ID={}", goodsId);
        longIdValidator.validate(goodsId);

        if (!goodsRepository.existsById(goodsId)) {
            log.error("Попытка удаления несуществующего товара: ID={}", goodsId);
            throw new GoodsNotFoundException("Товар не найден");
        }

        if (stockOperationsRepository.existsByGoodsId(goodsId)) {
            log.warn("Попытка удаления товара с операциями: ID={}", goodsId);
            throw new GoodsHasOperationsException("Товар имеет связанные операции");
        }

        goodsRepository.deleteById(goodsId);
        log.info("Товар ID={} удален", goodsId);
    }

    @Override
    public GoodsResponse getGoodsById(Long goodsId) {
        log.debug("Получение товара ID={}", goodsId);
        longIdValidator.validate(goodsId);

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> {
                    log.error("Товар не найден: ID={}", goodsId);
                    return new GoodsNotFoundException("Товар не найден");
                });

        return goodsMapper.toGoodsResponse(goods);
    }

    @Override
    public GoodsResponse setZeroQuantity(Long goodsId) {
        log.debug("Обнуление количества для товара ID={}", goodsId);
        longIdValidator.validate(goodsId);

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> {
                    log.error("Товар не найден: ID={}", goodsId);
                    return new GoodsNotFoundException("Товар не найден");
                });

        goods.setQuantity(0L);
        goodsRepository.save(goods);
        log.info("Количество товара ID={} обнулено", goodsId);
        return goodsMapper.toGoodsResponse(goods);
    }

    @Override
    public boolean existsById(Long goodsId) {
        longIdValidator.validate(goodsId);
        return goodsRepository.existsById(goodsId);
    }

    @Override
    public GoodsResponse updateGoodsQuantity(UpdateGoodsQuantityRequest request) {
        log.debug("Обновление количества товара ID={}, Тип={}, Количество={}",
                request.goodsId(), request.operationType(), request.quantity());

        longIdValidator.validate(request.goodsId());
        quantityValidator.validate(request.quantity());
        operationTypeValidator.validate(request.operationType());

        Goods goods = goodsRepository.findById(request.goodsId())
                .orElseThrow(() -> {
                    log.error("Товар не найден: ID={}", request.goodsId());
                    return new GoodsNotFoundException("Товар не найден");
                });

        Goods updatedGoods = goodsMapper.updateQuantity(goods, request.quantity(), request.operationType());
        goodsRepository.save(updatedGoods);
        log.info("Количество товара ID={} обновлено. Новое количество={}",
                request.goodsId(), updatedGoods.getQuantity());
        return goodsMapper.toGoodsResponse(updatedGoods);
    }

    @Override
    public GoodsResponse undoGoodsQuantity(UpdateGoodsQuantityRequest request) {
        log.debug("Отмена операции для товара ID={}, Тип={}, Количество={}",
                request.goodsId(), request.operationType(), request.quantity());

        longIdValidator.validate(request.goodsId());
        quantityValidator.validate(request.quantity());
        operationTypeValidator.validate(request.operationType());

        Goods goods = goodsRepository.findById(request.goodsId())
                .orElseThrow(() -> {
                    log.error("Товар не найден: ID={}", request.goodsId());
                    return new GoodsNotFoundException("Товар не найден");
                });

        Goods updatedGoods = goodsMapper.undoQuantity(goods, request.quantity(), request.operationType());
        goodsRepository.save(updatedGoods);
        log.info("Операция для товара ID={} отменена. Текущее количество={}",
                request.goodsId(), updatedGoods.getQuantity());
        return goodsMapper.toGoodsResponse(updatedGoods);
    }
}