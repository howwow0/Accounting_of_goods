package org.how_wow.infrastructure.ui.presenters.impl;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;
import org.how_wow.application.services.GoodsService;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.infrastructure.ui.presenters.StockOperationsListViewPresenter;
import org.how_wow.infrastructure.ui.view.StockOperationsListView;
import org.how_wow.infrastructure.ui.view.factories.StockOperationsDialogFactory;

@RequiredArgsConstructor
public class StockOperationsListViewPresenterImpl implements StockOperationsListViewPresenter {
    private final StockOperationsListView view;
    private final StockOperationService stockOperationService;
    private final GoodsService goodsService;
    private final StockOperationsDialogFactory stockOperationsDialogFactory;
    private final Long goodsId;

    @Override
    public void loadStockOperations(int page, int pageSize) {
        try {
            view.setOperationsButtonEnabled(false);
            PaginatedResult<StockOperationsResponse> stockOperations = stockOperationService.findStockByGoodsId(goodsId, (long) page, (long) pageSize);
            view.displayItems(stockOperations.getContent());
            view.setProductInfo(goodsService.getGoodsById(goodsId));
            view.setOperationsButtonEnabled(true);
        } catch (RuntimeException e) {
            view.showError(e.getMessage());
        }
    }

    @Override
    public void onItemSelected(StockOperationsResponse stockOperationsResponse) {
        view.setOperationsButtonEnabled(stockOperationsResponse != null);
    }

    @Override
    public void onApplyFilters() {
        try {
            view.setOperationsButtonEnabled(false);
            PaginatedResult<StockOperationsResponse> stockOperations = stockOperationService.findStocksByGoodsIdAndOperationTypeAndTimeDatesWithPagination(
                    goodsId,
                    view.getFilters(),
                    (long) view.getCurrentPage(),
                    (long) view.getPageSize());

            view.displayItems(stockOperations.getContent());
            view.setOperationsButtonEnabled(true);
        } catch (RuntimeException e) {
            view.showError(e.getMessage());
        }
    }

    @Override
    public void onResetFilters() {
        view.resetFilters();
        loadStockOperations(view.getCurrentPage(), view.getPageSize());
    }

    @Override
    public void onAddStockOperations() {
        stockOperationsDialogFactory.openStockOperationsCreateDialog(goodsId, stockOperationService);
    }

    @Override
    public void onDeleteStockOperations() {
        StockOperationsResponse selected = view.getSelectedItem();
        if (selected != null && view.showWarningDelete("Вы точно хотите удалить операцию с ID " + selected.id())) {
            try {
                stockOperationService.deleteStockById(selected.id());
            } catch (RuntimeException e) {
                view.showError(e.getMessage());
            }
        }
    }

    @Override
    public void onDeleteAllStockOperations() {
        if (view.showWarningDelete("Вы точно хотите удалить все операции?")) {
            try {
                stockOperationService.deleteAllStocksByGoodsId(goodsId);
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
    }

    @Override
    public void initialize() {
        view.setPresenter(this);
        loadStockOperations(view.getCurrentPage(), view.getPageSize());
        view.showView();
    }
}
