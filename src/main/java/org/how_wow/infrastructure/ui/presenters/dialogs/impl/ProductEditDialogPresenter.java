package org.how_wow.infrastructure.ui.presenters.dialogs.impl;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.presenters.dialogs.DialogPresenter;
import org.how_wow.infrastructure.ui.view.dialogs.DialogView;

@RequiredArgsConstructor
public class ProductEditDialogPresenter implements DialogPresenter {
    private final DialogView<GoodsResponse, GoodsRequest> view;
    private final GoodsService goodsService;
    private final Long productId;

    @Override
    public void onSave() {
        try {
            GoodsRequest goodsRequest = view.getData();
            GoodsResponse goodsResponse = goodsService.updateGoods(productId, goodsRequest);
            view.showSuccess("Товар " + goodsResponse.name() + " успешно изменен!");
            view.closeDialog();
        } catch (RuntimeException ex) {
            view.showError(ex.getMessage());
        }
    }

    @Override
    public void initialize() {
        try {
            view.setData(goodsService.getGoodsById(productId));
            view.setTitle("Редактирование товара");
            view.setSaveButtonText("Сохранить");
            view.showDialog();
        } catch (RuntimeException ex) {
            view.showError(ex.getMessage());
        }
    }
}
