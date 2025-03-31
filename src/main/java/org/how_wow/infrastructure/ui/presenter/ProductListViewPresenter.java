package org.how_wow.infrastructure.ui.presenter;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.view.ProductListView;
import org.how_wow.infrastructure.ui.view.ProductTableModel;


@RequiredArgsConstructor
public class ProductListViewPresenter {
    private final ProductListView view;
    private final GoodsService goodsService;

    public void bindOnUpdate() {
        view.getRefreshButton().addActionListener(e ->
        {
            PaginatedResult<GoodsResponse> goodsList = goodsService.getGoods(
                    Long.parseLong(String.valueOf(view.getPageSpinner().getValue())),
                    Long.parseLong(String.valueOf(view.getPageSizeCombo().getSelectedItem())));

            ProductTableModel ptm = new ProductTableModel(goodsList.getContent());
            view.getProductsTable().setModel(ptm);
        });
    }

}
