package org.how_wow.infrastructure.ui.presenter;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.request.FilterGoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.view.factories.ProductDialogFactory;
import org.how_wow.infrastructure.ui.view.ProductListView;
import org.how_wow.infrastructure.ui.view.impl.ProductDialogImpl;

import javax.swing.*;


@RequiredArgsConstructor
public class ProductListViewPresenter {
    private final ProductListView view;
    private final GoodsService goodsService;
    private final ProductDialogFactory productDialogFactory;

    public void bindOnResetFilter() {
        view.setResetFilterButtonAction(e -> {
            view.clearFilterFields();
            refreshData();
        });
    }

    public void bindOnEdit() {
        view.setEditButtonAction(e -> {
            long productId = view.getSelectedProductId();
            ProductDialogImpl productDialogImpl = productDialogFactory.createEditDialog(productId);
            productDialogImpl.setVisible(true);
            refreshData();
        });
    }

    public void bindOnAdd() {
        view.setAddButtonAction(e -> {
            ProductDialogImpl productDialogImpl = productDialogFactory.createCreateDialog();
            productDialogImpl.setVisible(true);
            refreshData();
        });
    }

    public void bindOnRefresh() {
        view.setRefreshButtonAction(e ->
                refreshData());
    }

    public void bindOnDelete() {
        view.setDeleteButtonAction(e -> {
            long productId = view.getSelectedProductId();
            int answer = JOptionPane.showConfirmDialog(view.getComponent(), "Вы точно хотите удалить товар с ID " + productId + "?", "Вы уверены?", JOptionPane.OK_CANCEL_OPTION);
            if (answer == JOptionPane.OK_OPTION) {
                try {
                    goodsService.deleteGoods(productId);
                    refreshData();
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(view.getComponent(), ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void bindOnFilter() {
        view.setApplyFilterButtonAction(e -> {
            FilterGoodsRequest filterGoodsRequest = FilterGoodsRequest.builder()
                    .searchAllFields(view.getSearchField())
                    .category(view.getCategoryFilter())
                    .build();
            //TODO решить проблему сброса фильтрации при обновлении данных
            PaginatedResult<GoodsResponse> goodsList = goodsService.getGoods(view.getCurrentPage(), view.getPageSize(), filterGoodsRequest);
            view.setProductList(goodsList.getContent());
        });
    }

    private void refreshData() {
        PaginatedResult<GoodsResponse> goodsList = goodsService.getGoods(view.getCurrentPage(), view.getPageSize());
        view.setProductList(goodsList.getContent());
    }

}
