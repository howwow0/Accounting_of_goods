package org.how_wow.config;

import org.how_wow.application.mappers.GoodsMapper;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.impl.GoodsServiceImpl;
import org.how_wow.application.validators.*;
import org.how_wow.domain.repository.GoodsRepository;
import org.how_wow.domain.repository.StockOperationsRepository;
import org.how_wow.infrastructure.persistense.InMemoryGoodsRepository;
import org.how_wow.infrastructure.persistense.InMemoryStockOperationsRepository;
import org.how_wow.infrastructure.ui.presenter.ProductListViewPresenter;
import org.how_wow.infrastructure.ui.view.factories.ProductDialogFactory;
import org.how_wow.infrastructure.ui.view.factories.SwingProductDialogFactory;
import org.how_wow.infrastructure.ui.view.impl.ProductListViewImpl;

import javax.swing.*;
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

        SwingUtilities.invokeLater(() -> {
            ProductListViewImpl productListViewImpl = new ProductListViewImpl();
            ProductDialogFactory productDialogFactory = new SwingProductDialogFactory(productListViewImpl, goodsService);
            ProductListViewPresenter productListViewPresenter = new ProductListViewPresenter(productListViewImpl, goodsService, productDialogFactory);
            productListViewPresenter.bindOnAdd();
            productListViewPresenter.bindOnEdit();
            productListViewPresenter.bindOnRefresh();
            productListViewPresenter.bindOnResetFilter();
            productListViewPresenter.bindOnDelete();
            productListViewPresenter.bindOnFilter();
            productListViewImpl.setVisible(true);
        });
    }
}
