package org.how_wow.infrastructure.ui.view.factories;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.infrastructure.ui.presenters.StockOperationsListViewPresenter;
import org.how_wow.infrastructure.ui.presenters.impl.StockOperationsListViewPresenterImpl;
import org.how_wow.infrastructure.ui.view.StockOperationsListView;
import org.how_wow.infrastructure.ui.view.impl.StockOperationsListViewImpl;

import javax.swing.*;
import java.awt.*;


@RequiredArgsConstructor
public class StockOperationsViewFactory {
    private final StockOperationService stockOperationService;
    private final GoodsService goodsService;
    private final StockOperationsDialogFactory stockOperationsDialogFactory;
    private final Frame parent;

    public void openStockOperationsView(GoodsResponse goodsResponse) {
        SwingUtilities.invokeLater(() -> {
            StockOperationsListView view = new StockOperationsListViewImpl(parent);
            StockOperationsListViewPresenter presenter = new StockOperationsListViewPresenterImpl(
                    view,
                    stockOperationService,
                    goodsService,
                    stockOperationsDialogFactory,
                    goodsResponse.id());
            view.setPresenter(presenter);
            view.setProductInfo(goodsResponse);
            presenter.initialize();
        });

    }
}
