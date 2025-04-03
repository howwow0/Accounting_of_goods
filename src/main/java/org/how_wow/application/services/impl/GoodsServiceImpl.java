package org.how_wow.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.how_wow.application.dto.goods.request.FilterGoodsRequest;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.request.UpdateGoodsQuantityRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.mappers.GoodsMapper;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.validators.Validator;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.domain.model.Goods;
import org.how_wow.domain.repository.GoodsRepository;
import org.how_wow.domain.repository.StockOperationsRepository;
import org.how_wow.exceptions.*;

import java.math.BigDecimal;

/**
 * Реализация сервиса для управления товарами.
 * <p>
 * Этот класс реализует методы интерфейса {@link GoodsService}, предоставляя функциональность для
 * создания, обновления, удаления и получения товаров. Также обеспечивается обработка операций
 * с количеством товара и управление их состоянием на складе.
 * </p>
 */
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

    /**
     * Создает новый товар.
     * <p>
     * Метод создает новый товар в базе данных, выполняет валидацию входных данных и возвращает
     * информацию о созданном товаре.
     * </p>
     *
     * @param goodsRequest данные для создания товара
     * @return {@link GoodsResponse} с информацией о созданном товаре
     * @throws ValidationException если валидация данных не прошла
     */
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

    /**
     * Получает список товаров с пагинацией.
     * <p>
     * Метод возвращает товары с пагинацией, которые соответствуют указанным номерам страницы
     * и размерам страницы.
     * </p>
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     * @return {@link PaginatedResult} список товаров
     */
    @Override
    public PaginatedResult<GoodsResponse> getGoods(Long pageNumber, Long pageSize) {
        log.debug("Получение товаров. Страница={}, Размер={}", pageNumber, pageSize);
        pageNumberAndPageSizeValidator.validate(pageNumber);
        pageNumberAndPageSizeValidator.validate(pageSize);

        return goodsMapper.toPaginatedGoodsResponse(goodsRepository.findAllWithPaging(pageNumber, pageSize));
    }

    /**
     * Получает список товаров с пагинацией и фильтрацией.
     * <p>
     * Метод возвращает список товаров с пагинацией, с возможностью фильтрации по категории
     * и наименованию.
     * </p>
     *
     * @param pageNumber    номер страницы
     * @param pageSize      размер страницы
     * @param filterRequest фильтры для получения товаров
     * @return {@link PaginatedResult} список товаров с фильтрацией
     */
    @Override
    public PaginatedResult<GoodsResponse> getGoods(Long pageNumber, Long pageSize, FilterGoodsRequest filterRequest) {
        log.debug("Получение товаров с фильтрацией. Страница={}, Размер={}, Фильтр={}",
                pageNumber, pageSize, filterRequest);
        return goodsMapper.toPaginatedGoodsResponse(goodsRepository.findContainsByNameAndCategoryWithPaging(pageNumber, pageSize,
                filterRequest.name(), filterRequest.category()));
    }

    /**
     * Обновляет товар по его ID.
     * <p>
     * Метод обновляет существующий товар в базе данных, если товар с указанным ID найден,
     * и возвращает информацию о нем.
     * </p>
     *
     * @param goodsId      ID товара для обновления
     * @param goodsRequest новые данные для товара
     * @return {@link GoodsResponse} с обновленной информацией о товаре
     * @throws ValidationException    если валидация данных не пройдена
     * @throws GoodsNotFoundException если товар с указанным ID не найден
     */
    @Override
    public GoodsResponse updateGoods(Long goodsId, GoodsRequest goodsRequest) {
        log.debug("Обновление товара ID={}", goodsId);
        longIdValidator.validate(goodsId);
        nameValidator.validate(goodsRequest.name());
        categoryValidator.validate(goodsRequest.category());
        priceValidator.validate(goodsRequest.price());

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new GoodsNotFoundException("Товар с ID " + goodsId + " не найден"));

        Goods updatedGoods = goodsRepository.save(goodsMapper.updateGoods(goods, goodsRequest));
        log.info("Товар ID={} успешно обновлен", goodsId);
        return goodsMapper.toGoodsResponse(updatedGoods);
    }

    /**
     * Удаляет товар по его ID.
     * <p>
     * Метод удаляет товар с указанным ID, если товар найден и у него нет связанных операций.
     * </p>
     *
     * @param goodsId ID товара для удаления
     * @throws GoodsNotFoundException      если товар с указанным ID не найден
     * @throws GoodsHasOperationsException если товар имеет связанные операции
     */
    @Override
    public void deleteGoods(Long goodsId) {
        log.debug("Попытка удаления товара ID={}", goodsId);
        longIdValidator.validate(goodsId);

        if (!goodsRepository.existsById(goodsId)) {
            log.error("Попытка удаления несуществующего товара: ID={}", goodsId);
            throw new GoodsNotFoundException("Товар с ID " + goodsId + " не найден");
        }

        if (stockOperationsRepository.existsByGoodsId(goodsId)) {
            log.warn("Попытка удаления товара с операциями: ID={}", goodsId);
            throw new GoodsHasOperationsException("Товар с ID " + goodsId + " имеет связанные операции");
        }

        goodsRepository.deleteById(goodsId);
        log.info("Товар ID={} удален", goodsId);
    }

    /**
     * Получает товар по его ID.
     * <p>
     * Метод возвращает товар с указанным ID, если он существует в базе данных.
     * </p>
     *
     * @param goodsId ID товара
     * @return {@link GoodsResponse} информация о товаре
     * @throws GoodsNotFoundException если товар с указанным ID не найден
     */
    @Override
    public GoodsResponse getGoodsById(Long goodsId) {
        log.debug("Получение товара ID={}", goodsId);
        longIdValidator.validate(goodsId);

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new GoodsNotFoundException("Товар с ID " + goodsId + " не найден"));

        return goodsMapper.toGoodsResponse(goods);
    }

    /**
     * Обнуляет количество товара.
     * <p>
     * Метод обнуляет количество товара на складе для товара с указанным ID.
     * </p>
     *
     * @param goodsId ID товара
     * @return {@link GoodsResponse} товар с нулевым количеством
     * @throws GoodsNotFoundException если товар с указанным ID не найден
     */
    @Override
    public GoodsResponse setZeroQuantity(Long goodsId) {
        log.debug("Обнуление количества для товара ID={}", goodsId);
        longIdValidator.validate(goodsId);

        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new GoodsNotFoundException("Товар с ID " + goodsId + " не найден"));

        goods.setQuantity(0L);
        goodsRepository.save(goods);
        log.info("Количество товара ID={} обнулено", goodsId);
        return goodsMapper.toGoodsResponse(goods);
    }

    /**
     * Проверяет существование товара по ID.
     * <p>
     * Метод проверяет, существует ли товар с указанным ID в базе данных.
     * </p>
     *
     * @param goodsId ID товара
     * @return true, если товар существует, иначе false
     */
    @Override
    public boolean existsById(Long goodsId) {
        longIdValidator.validate(goodsId);
        return goodsRepository.existsById(goodsId);
    }

    /**
     * Обновляет количество товара.
     * <p>
     * Метод обновляет количество товара на складе, учитывая тип операции (пополнение или списание).
     * </p>
     *
     * @param updateGoodsQuantityRequest запрос с данными для обновления
     * @return {@link GoodsResponse} с обновленным количеством товара
     * @throws GoodsNotFoundException     если товар с указанным ID не найден
     * @throws InsufficientGoodsException если на складе недостаточно товара
     */
    @Override
    public GoodsResponse updateGoodsQuantity(UpdateGoodsQuantityRequest updateGoodsQuantityRequest) {
        log.debug("Обновление количества товара ID={}, Тип={}, Количество={}",
                updateGoodsQuantityRequest.goodsId(), updateGoodsQuantityRequest.operationType(), updateGoodsQuantityRequest.quantity());

        longIdValidator.validate(updateGoodsQuantityRequest.goodsId());
        quantityValidator.validate(updateGoodsQuantityRequest.quantity());
        operationTypeValidator.validate(updateGoodsQuantityRequest.operationType());

        Goods goods = goodsRepository.findById(updateGoodsQuantityRequest.goodsId())
                .orElseThrow(() -> new GoodsNotFoundException("Товар с ID " + updateGoodsQuantityRequest.goodsId() + " не найден"));

        Goods updatedGoods = goodsMapper.updateQuantity(goods, updateGoodsQuantityRequest.quantity(), updateGoodsQuantityRequest.operationType());
        goodsRepository.save(updatedGoods);
        log.info("Количество товара ID={} обновлено. Новое количество={}",
                updateGoodsQuantityRequest.goodsId(), updatedGoods.getQuantity());
        return goodsMapper.toGoodsResponse(updatedGoods);
    }

    /**
     * Отменяет изменение количества товара.
     * <p>
     * Метод отменяет изменения количества товара, если текущее количество на складе меньше
     * отменяемого количества.
     * </p>
     *
     * @param updateGoodsQuantityRequest запрос с данными для отмены операции
     * @return {@link GoodsResponse} с обновленным количеством товара
     * @throws GoodsNotFoundException если товар с указанным ID не найден
     * @throws UndoOperationException если текущее количество товара на складе меньше отменяемого
     */
    @Override
    public GoodsResponse undoGoodsQuantity(UpdateGoodsQuantityRequest updateGoodsQuantityRequest) {
        log.debug("Отмена операции для товара ID={}, Тип={}, Количество={}",
                updateGoodsQuantityRequest.goodsId(), updateGoodsQuantityRequest.operationType(), updateGoodsQuantityRequest.quantity());

        longIdValidator.validate(updateGoodsQuantityRequest.goodsId());
        quantityValidator.validate(updateGoodsQuantityRequest.quantity());
        operationTypeValidator.validate(updateGoodsQuantityRequest.operationType());

        Goods goods = goodsRepository.findById(updateGoodsQuantityRequest.goodsId())
                .orElseThrow(() -> new GoodsNotFoundException("Товар с ID " + updateGoodsQuantityRequest.goodsId() + " не найден"));

        Goods updatedGoods = goodsMapper.undoQuantity(goods, updateGoodsQuantityRequest.quantity(), updateGoodsQuantityRequest.operationType());
        goodsRepository.save(updatedGoods);
        log.info("Операция для товара ID={} отменена. Текущее количество={}",
                updateGoodsQuantityRequest.goodsId(), updatedGoods.getQuantity());
        return goodsMapper.toGoodsResponse(updatedGoods);
    }
}
