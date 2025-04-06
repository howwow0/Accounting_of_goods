package org.how_wow.infrastructure.ui.presenters;


import org.how_wow.application.dto.goods.response.GoodsResponse;


public interface ProductListViewPresenter {
    void loadProducts(int page, int pageSize);

    void onItemSelected(GoodsResponse product);

    void onApplyFilters();

    void onResetFilters();

    void onAddProduct();

    void onEditProduct();

    void onDeleteProduct();

    void onShowOperations();

    void initialize();
}
