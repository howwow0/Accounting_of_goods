package org.how_wow.infrastructure.ui.view;

import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.stockOperations.request.FilterStockOperationsRequest;
import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;
import org.how_wow.infrastructure.ui.presenters.StockOperationsListViewPresenter;

import java.util.List;

public interface StockOperationsListView {
    void displayItems(List<StockOperationsResponse> items);

    void setOperationsButtonEnabled(boolean enabled);

    StockOperationsResponse getSelectedItem();

    void showError(String message);

    boolean showWarningDelete(String message);

    void resetFilters();

    FilterStockOperationsRequest getFilters();

    void setPresenter(StockOperationsListViewPresenter presenter);

    int getCurrentPage();

    int getPageSize();

    void showView();

    void setProductInfo(GoodsResponse goods);
}
