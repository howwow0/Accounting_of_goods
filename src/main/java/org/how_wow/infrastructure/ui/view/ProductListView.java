package org.how_wow.infrastructure.ui.view;

import org.how_wow.application.dto.goods.request.FilterGoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.infrastructure.ui.presenters.ProductListViewPresenter;

import java.util.List;

public interface ProductListView {
    void displayItems(List<GoodsResponse> items);

    void setOperationsButtonEnabled(boolean enabled);

    GoodsResponse getSelectedItem();

    void showError(String message);

    boolean showWarningDelete(String message);

    void resetFilters();

    FilterGoodsRequest getFilters();

    void setPresenter(ProductListViewPresenter presenter);

    int getCurrentPage();

    int getPageSize();

    void showView();
}
