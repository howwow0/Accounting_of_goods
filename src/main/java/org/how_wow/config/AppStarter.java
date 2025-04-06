package org.how_wow.config;

import org.how_wow.application.mappers.GoodsMapper;
import org.how_wow.application.mappers.StockOperationsMapper;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.application.services.impl.GoodsServiceImpl;
import org.how_wow.application.services.impl.StockOperationsServiceImpl;
import org.how_wow.application.validators.*;
import org.how_wow.domain.repository.GoodsRepository;
import org.how_wow.domain.repository.StockOperationsRepository;
import org.how_wow.infrastructure.persistense.InMemoryGoodsRepository;
import org.how_wow.infrastructure.persistense.InMemoryStockOperationsRepository;
import org.how_wow.infrastructure.ui.presenters.ProductListViewPresenter;
import org.how_wow.infrastructure.ui.presenters.impl.ProductListViewPresenterImpl;
import org.how_wow.infrastructure.ui.view.ProductListView;
import org.how_wow.infrastructure.ui.view.factories.ProductDialogFactory;
import org.how_wow.infrastructure.ui.view.factories.StockOperationsDialogFactory;
import org.how_wow.infrastructure.ui.view.factories.StockOperationsViewFactory;
import org.how_wow.infrastructure.ui.view.impl.ProductListViewImpl;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class AppStarter {
    public void Start() {
        GoodsRepository goodsRepository = new InMemoryGoodsRepository(new HashMap<>());
        StockOperationsRepository stockOperationsRepository = new InMemoryStockOperationsRepository(new HashMap<>());
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

        StockOperationService stockOperationsService = new StockOperationsServiceImpl(stockOperationsRepository,
                new StockOperationsMapper(),
                goodsService,
                idValidator,
                quantityValidator,
                operationTypeValidator,
                pageSizeAndPageNumberValidator
        );

        SwingUtilities.invokeLater(() -> {
            ProductListView productListView = new ProductListViewImpl();

            StockOperationsViewFactory stockOperationsViewFactory = new StockOperationsViewFactory(
                    stockOperationsService,
                    goodsService,
                    new StockOperationsDialogFactory((Frame) productListView),
                    (Frame) productListView);

            ProductDialogFactory productDialogFactory = new ProductDialogFactory(
                    (Frame) productListView);

            ProductListViewPresenter productListViewPresenter = new ProductListViewPresenterImpl(
                    productListView,
                    goodsService,
                    productDialogFactory,
                    stockOperationsViewFactory);

            productListViewPresenter.initialize();
        });
    }
}
