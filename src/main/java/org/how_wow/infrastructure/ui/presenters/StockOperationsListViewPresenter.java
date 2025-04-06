package org.how_wow.infrastructure.ui.presenters;

import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;

public interface StockOperationsListViewPresenter {
    void loadStockOperations(int page, int pageSize);

    void onItemSelected(StockOperationsResponse stockOperationsResponse);

    void onApplyFilters();

    void onResetFilters();

    void onAddStockOperations();

    void onDeleteStockOperations();

    void onDeleteAllStockOperations();

    void initialize();
}
