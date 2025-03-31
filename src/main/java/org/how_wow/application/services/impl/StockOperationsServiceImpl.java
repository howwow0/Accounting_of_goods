package org.how_wow.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.how_wow.application.dto.StockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.StockOperations.response.StockOperationsResponse;
import org.how_wow.application.dto.goods.request.UpdateGoodsQuantityRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.mappers.StockOperationsMapper;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.application.validators.Validator;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.domain.model.StockOperations;
import org.how_wow.domain.repository.StockOperationsRepository;
import org.how_wow.exceptions.StockOperationsNotFoundException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class StockOperationsServiceImpl implements StockOperationService {
    private final StockOperationsRepository stockOperationsRepository;
    private final StockOperationsMapper stockOperationsMapper;
    private final GoodsService goodsService;
    private final Validator<Long> longIdValidator;
    private final Validator<Long> quantityValidator;
    private final Validator<OperationType> operationTypeValidator;
    private final Validator<Long> pageNumberAndPageSizeValidator;

    @Override
    public void deleteAllStocksByGoodsId(Long goodsId) {
        log.info("Удаление всех операций для товара ID={}", goodsId);
        longIdValidator.validate(goodsId);

        stockOperationsRepository.deleteAllByGoodsId(goodsId);
        goodsService.setZeroQuantity(goodsId);
        log.info("Все операции товара ID={} удалены", goodsId);
    }

    @Override
    public StockOperationsResponse createStock(StockOperationsRequest request) {
        log.debug("Создание операции. Товар ID={}, Тип={}, Количество={}",
                request.goodsId(), request.operationType(), request.quantity());

        longIdValidator.validate(request.goodsId());
        quantityValidator.validate(request.quantity());
        operationTypeValidator.validate(request.operationType());

        StockOperations operation = stockOperationsMapper.toStockOperations(request);
        goodsService.updateGoodsQuantity(new UpdateGoodsQuantityRequest(
                request.goodsId(), request.quantity(), request.operationType()));

        StockOperations savedOperation = stockOperationsRepository.save(operation);
        log.info("Создана операция ID={} для товара ID={}",
                savedOperation.getId(), request.goodsId());

        return stockOperationsMapper.toStockOperationsResponse(savedOperation);
    }

    @Override
    public PaginatedResult<StockOperationsResponse> findStockByGoodsId(Long goodsId, Long pageNumber, Long pageSize) {
        log.debug("Поиск операций по товару ID={}. Страница={}, Размер={}",
                goodsId, pageNumber, pageSize);

        longIdValidator.validate(goodsId);
        pageNumberAndPageSizeValidator.validate(pageNumber);
        pageNumberAndPageSizeValidator.validate(pageSize);

        return stockOperationsMapper.toPaginatedGoodsResponse(
                stockOperationsRepository.findByGoodsIdWithPaging(goodsId, pageNumber, pageSize));
    }

    @Override
    public void deleteStockById(Long stockId) {
        log.debug("Удаление операции ID={}", stockId);
        longIdValidator.validate(stockId);

        StockOperations operation = stockOperationsRepository.findById(stockId)
                .orElseThrow(() -> {
                    log.error("Операция не найдена: ID={}", stockId);
                    return new StockOperationsNotFoundException("Операция не найдена");
                });

        goodsService.undoGoodsQuantity(new UpdateGoodsQuantityRequest(
                operation.getGoodsId(),
                operation.getQuantity(),
                operation.getOperationType()));

        stockOperationsRepository.deleteById(stockId);
        log.info("Операция ID={} удалена", stockId);
    }

    @Override
    public boolean existsByGoodsId(Long goodsId) {
        return stockOperationsRepository.existsByGoodsId(goodsId);
    }
}