package org.how_wow.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.how_wow.application.dto.StockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.StockOperations.response.StockOperationsResponse;
import org.how_wow.application.dto.goods.request.UpdateGoodsQuantityRequest;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.mappers.StockOperationsMapper;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.application.validators.Validator;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.domain.model.StockOperations;
import org.how_wow.domain.repository.StockOperationsRepository;
import org.how_wow.exceptions.StockOperationsNotFoundException;

/**
 * Реализация сервиса для управления операциями со складом.
 * <p>
 * Эта реализация включает логику для создания операций пополнения и списания товара на складе,
 * а также для удаления операций и получения информации о них.
 * </p>
 */
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

    /**
     * Удаляет все операции по товару.
     * <p>
     * Метод удаляет все операции (пополнения и списания) по товару с указанным ID
     * и устанавливает количество товара на складе в 0.
     * </p>
     *
     * @param goodsId ID товара для удаления всех операций.
     */
    @Override
    public void deleteAllStocksByGoodsId(Long goodsId) {
        log.info("Удаление всех операций для товара ID={}", goodsId);
        longIdValidator.validate(goodsId);

        stockOperationsRepository.deleteAllByGoodsId(goodsId);
        goodsService.setZeroQuantity(goodsId);
        log.info("Все операции товара ID={} удалены", goodsId);
    }

    /**
     * Создает новую операцию на складе.
     * <p>
     * Этот метод выполняет создание операции пополнения или списания товара, обновляет
     * количество товара и сохраняет информацию о операции в базе данных.
     * </p>
     *
     * @param request Данные для создания операции.
     * @return Ответ с информацией о созданной операции.
     */
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

    /**
     * Получает список операций по товару с пагинацией.
     * <p>
     * Метод возвращает список операций пополнения и списания для товара с указанным ID,
     * с возможностью пагинации (страничный вывод).
     * </p>
     *
     * @param goodsId ID товара для поиска операций.
     * @param pageNumber Номер страницы.
     * @param pageSize Размер страницы.
     * @return Пагинированный список операций.
     */
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

    /**
     * Удаляет операцию по ее ID.
     * <p>
     * Метод удаляет операцию по ее уникальному ID и отменяет изменения в количестве товара.
     * Если операция не найдена, выбрасывается исключение.
     * </p>
     *
     * @param stockId ID операции для удаления.
     * @throws StockOperationsNotFoundException Если операция с таким ID не найдена.
     */
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

    /**
     * Проверяет существование операций по товару.
     * <p>
     * Этот метод проверяет наличие операций пополнения или списания для товара с указанным ID.
     * </p>
     *
     * @param goodsId ID товара для проверки наличия операций.
     * @return true, если операции существуют, иначе false.
     */
    @Override
    public boolean existsByGoodsId(Long goodsId) {
        return stockOperationsRepository.existsByGoodsId(goodsId);
    }
}
