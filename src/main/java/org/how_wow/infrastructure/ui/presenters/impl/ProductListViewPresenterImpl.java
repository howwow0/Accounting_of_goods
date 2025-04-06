package org.how_wow.infrastructure.ui.presenters.impl;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.presenters.ProductListViewPresenter;
import org.how_wow.infrastructure.ui.view.ProductListView;
import org.how_wow.infrastructure.ui.view.factories.ProductDialogFactory;
import org.how_wow.infrastructure.ui.view.factories.StockOperationsViewFactory;


@RequiredArgsConstructor
public class ProductListViewPresenterImpl implements ProductListViewPresenter {
    private final ProductListView view;
    private final GoodsService goodsService;
    private final ProductDialogFactory productDialogFactory;
    private final StockOperationsViewFactory stockOperationsViewFactory;

    @Override
    public void initialize() {
        view.setPresenter(this);
        loadProducts(view.getCurrentPage(), view.getPageSize());
        view.showView();
    }

    @Override
    public void loadProducts(int page, int size) {
        try {
            view.setOperationsButtonEnabled(false);
            PaginatedResult<GoodsResponse> goods = goodsService.getGoods((long) page, (long) size);
            view.displayItems(goods.getContent());
            view.setOperationsButtonEnabled(true);
        } catch (RuntimeException e) {
            view.showError(e.getMessage());
        }
    }

    @Override
    public void onAddProduct() {
        productDialogFactory.openProductCreateDialog(goodsService);
    }

    @Override
    public void onEditProduct() {
        GoodsResponse selected = view.getSelectedItem();
        if (selected != null) {
            try {
                productDialogFactory.openProductEditDialog(selected.id(), goodsService);
            } catch (RuntimeException e) {
                view.showError(e.getMessage());
            }
        }
    }

    @Override
    public void onDeleteProduct() {
        GoodsResponse selected = view.getSelectedItem();
        if (selected != null && view.showWarningDelete("Вы точно хотите удалить товар с ID " + selected.id())) {
            try {
                goodsService.deleteGoods(selected.id());
            } catch (RuntimeException e) {
                view.showError(e.getMessage());
            }
        }
    }

    @Override
    public void onShowOperations() {
        GoodsResponse selected = view.getSelectedItem();
        if (selected != null) {
            try {
                stockOperationsViewFactory.openStockOperationsView(selected);
            } catch (RuntimeException e) {
                view.showError(e.getMessage());
            }
        }
    }

    @Override
    public void onItemSelected(GoodsResponse selectedItem) {
        view.setOperationsButtonEnabled(selectedItem != null);
    }

    @Override
    public void onApplyFilters() {
        try {
            view.setOperationsButtonEnabled(false);
            PaginatedResult<GoodsResponse> goods = goodsService.getGoods((long) view.getCurrentPage(), (long) view.getPageSize(), view.getFilters());
            view.displayItems(goods.getContent());
            view.setOperationsButtonEnabled(true);
        } catch (RuntimeException e) {
            view.showError(e.getMessage());
        }
    }

    @Override
    public void onResetFilters() {
        view.resetFilters();
        loadProducts(view.getCurrentPage(), view.getPageSize());
    }

}