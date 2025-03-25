package org.how_wow.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.how_wow.application.dto.StockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.StockOperations.response.StockOperationsResponse;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.mappers.GoodsMapper;
import org.how_wow.application.mappers.StockOperationsMapper;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.application.validators.Validator;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.domain.model.Goods;
import org.how_wow.domain.model.StockOperations;
import org.how_wow.domain.repository.GoodsRepository;
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
        log.info("Проверка валидности ID товара для удаления: {}", goodsId);
        longIdValidator.validate(goodsId);
        log.info("Удаление всех операций товара с ID: {}", goodsId);
        stockOperationsRepository.deleteAllByGoodsId(goodsId);
        if (goodsService.existsById(goodsId))
            goodsService.setZeroQuantity(goodsId);
        log.info("Все операции товара с ID: {} удалены", goodsId);
    }

    @Override
    public StockOperationsResponse createStock(StockOperationsRequest stockOperationsRequest) {
        log.info("Проверка валидности запроса на создание операции со складом: {}", stockOperationsRequest);
        longIdValidator.validate(stockOperationsRequest.goodsId());
        quantityValidator.validate(stockOperationsRequest.quantity());
        operationTypeValidator.validate(stockOperationsRequest.operationType());

        log.info("Создание операции со складом: {}", stockOperationsRequest);
        StockOperations stockOperationsForSave = stockOperationsMapper.toStockOperations(stockOperationsRequest);
        GoodsResponse goodsResponse = goodsService.getGoodsById(stockOperationsForSave.getGoodsId());
        Long quantity = stockOperationsForSave.getQuantity();
        OperationType operationType = stockOperationsForSave.getOperationType();
        goodsService.updateGoodsQuantity(goodsResponse.id(), quantity, operationType);
        StockOperations savedStockOperations = stockOperationsRepository.save(stockOperationsForSave);

        log.info("Операция со складом создана с ID: {}", savedStockOperations.getId());
        return stockOperationsMapper.toStockOperationsResponse(savedStockOperations);
    }

    @Override
    public PaginatedResult<StockOperationsResponse> findStockByGoodsId(Long goodsId, Long pageNumber, Long pageSize) {
        log.info("Проверка валидности номера страницы и размера страницы: pageNumber={}, pageSize={}", pageNumber, pageSize);
        longIdValidator.validate(goodsId);
        pageNumberAndPageSizeValidator.validate(pageNumber);
        pageNumberAndPageSizeValidator.validate(pageSize);
        log.info("Получение списка товаров с пагинацией: pageNumber={}, pageSize={}", pageNumber, pageSize);
        return stockOperationsMapper.toPaginatedGoodsResponse(stockOperationsRepository.findByGoodsIdWithPaging(goodsId, pageNumber, pageSize));
    }

    @Override
    public void deleteStockById(Long stockId) {
        log.info("Проверка валидности ID операции со складом для удаления: {}", stockId);
        longIdValidator.validate(stockId);
        Optional<StockOperations> stockOperations = stockOperationsRepository.findById(stockId);
        if (stockOperations.isEmpty()) {
            log.error("Операция со складом с ID: {} не найдена", stockId);
            throw new StockOperationsNotFoundException("Не найдена операция со складом с ID: " + stockId);
        }
        log.info("Удаление операции со складом с ID: {}", stockId);
        stockOperationsRepository.deleteById(stockId);
        StockOperations deletedStockOperations = stockOperations.get();
        Long quantity = deletedStockOperations.getQuantity();
        OperationType operationType = deletedStockOperations.getOperationType();
        // Товар ДОЛЖЕН существовать - если нет, это ошибка данных
        goodsService.redoGoodsQuantity(deletedStockOperations.getGoodsId(), quantity, operationType);
        log.info("Операция со складом с ID: {} удалена", stockId);
    }

    @Override
    public boolean existsByGoodsId(Long goodsId) {
        return stockOperationsRepository.existsByGoodsId(goodsId);
    }
}
