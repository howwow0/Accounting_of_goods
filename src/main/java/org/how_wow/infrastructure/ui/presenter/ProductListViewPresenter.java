package org.how_wow.infrastructure.ui.presenter;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.request.FilterGoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.presenter.utils.SwingUtils;
import org.how_wow.infrastructure.ui.view.ProductListView;
import org.how_wow.infrastructure.ui.view.factories.ProductDialogFactory;
import org.how_wow.infrastructure.ui.view.impl.ProductDialogImpl;

import javax.swing.*;

@RequiredArgsConstructor
public class ProductListViewPresenter {
    private final ProductListView view;
    private final GoodsService goodsService;
    private final ProductDialogFactory productDialogFactory;

    public void bindOnResetFilter() {
        view.setResetFilterButtonAction(_ -> {
            view.clearFilterFields();
            refreshData();
        });
    }

    public void bindOnEdit() {
        view.setEditButtonAction(_ -> {
            long productId = view.getSelectedProductId();
            ProductDialogImpl productDialogImpl = productDialogFactory.createEditDialog(productId);
            productDialogImpl.setVisible(true);
            refreshData();
        });
    }

    public void bindOnAdd() {
        view.setAddButtonAction(_ -> {
            ProductDialogImpl productDialogImpl = productDialogFactory.createCreateDialog();
            productDialogImpl.setVisible(true);
            refreshData();
        });
    }

    public void bindOnRefresh() {
        view.setRefreshButtonAction(_ -> refreshData());
    }

    public void bindOnDelete() {
        view.setDeleteButtonAction(_ -> {
            long productId = view.getSelectedProductId();
            if (JOptionPane.showConfirmDialog(view.getComponent(), "Вы точно хотите удалить товар с ID " + productId + "?",
                    "Подтверждение удаления",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
                SwingUtils.executeAsync(() -> {
                    try {
                        goodsService.deleteGoods(productId);
                        refreshData();
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(view.getComponent(), ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        });
    }

    public void bindOnFilter() {
        view.setApplyFilterButtonAction(_ -> {
            view.setLoading(true);
            SwingUtils.executeAsync(() -> {
                try {
                    FilterGoodsRequest request = FilterGoodsRequest.builder()
                            .name(view.getNameFieldText())
                            .category(view.getCategoryFieldText())
                            .build();

                    PaginatedResult<GoodsResponse> goodsList = goodsService.getGoods(
                            view.getCurrentPage(),
                            view.getPageSize(),
                            request
                    );

                    SwingUtilities.invokeLater(() -> view.setProductList(goodsList.getContent()));
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(view.getComponent(), ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }, () -> view.setLoading(false));
        });
    }

    private void refreshData() {
        view.setLoading(true);
        SwingUtils.executeAsync(() -> {
            try {
                PaginatedResult<GoodsResponse> goodsList = goodsService.getGoods(
                        view.getCurrentPage(),
                        view.getPageSize()
                );

                SwingUtilities.invokeLater(() -> view.setProductList(goodsList.getContent()));
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(view.getComponent(), ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }, () -> view.setLoading(false));
    }
}
