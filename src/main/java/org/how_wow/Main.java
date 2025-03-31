package org.how_wow;


import org.how_wow.application.mappers.GoodsMapper;
import org.how_wow.application.mappers.StockOperationsMapper;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.application.services.impl.GoodsServiceImpl;
import org.how_wow.application.services.impl.StockOperationsServiceImpl;
import org.how_wow.application.validators.*;
import org.how_wow.domain.model.Goods;
import org.how_wow.domain.repository.GoodsRepository;
import org.how_wow.domain.repository.StockOperationsRepository;
import org.how_wow.infrastructure.persistense.InMemoryGoodsRepository;
import org.how_wow.infrastructure.persistense.InMemoryStockOperationsRepository;
import org.how_wow.infrastructure.ui.presenter.ProductListViewPresenter;
import org.how_wow.infrastructure.ui.view.ProductListView;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        GoodsRepository goodsRepository = new InMemoryGoodsRepository(new HashMap<>());
        StockOperationsRepository stockOperationsRepository = new InMemoryStockOperationsRepository(new HashMap<>());

        goodsRepository.save(Goods.builder()
                .name("asd")
                .quantity(1L)
                .category("asdasd")
                .price(BigDecimal.ONE)
                .build());
        goodsRepository.save(Goods.builder()
                .name("asd")
                .quantity(1L)
                .category("asdasd")
                .price(BigDecimal.ONE)
                .build());
        goodsRepository.save(Goods.builder()
                .name("asd")
                .quantity(1L)
                .category("asdasd")
                .price(BigDecimal.ONE)
                .build());

        LongIdValidator idValidator = new LongIdValidator();
        NameValidator nameValidator = new NameValidator();
        CategoryValidator categoryValidator = new CategoryValidator();
        PriceValidator priceValidator = new PriceValidator();
        QuantityValidator quantityValidator = new QuantityValidator();
        OperationTypeValidator operationTypeValidator = new OperationTypeValidator();
        PageSizeAndPageNumberValidator pageSizeAndPageNumberValidator = new PageSizeAndPageNumberValidator();

        GoodsService goodsService = new GoodsServiceImpl(stockOperationsRepository,
                goodsRepository,
                new GoodsMapper(),
                idValidator,
                nameValidator,
                categoryValidator,
                priceValidator,
                pageSizeAndPageNumberValidator,
                quantityValidator,
                operationTypeValidator
        );
        SwingUtilities.invokeLater(() -> {
            ProductListView productListView = new ProductListView();
            productListView.setVisible(true);
            ProductListViewPresenter productListViewPresenter = new ProductListViewPresenter(productListView, goodsService);
            productListViewPresenter.bindOnUpdate();
        });
    }
}