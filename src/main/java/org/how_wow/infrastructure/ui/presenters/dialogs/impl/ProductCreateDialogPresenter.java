package org.how_wow.infrastructure.ui.presenters.dialogs.impl;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.presenters.dialogs.DialogPresenter;
import org.how_wow.infrastructure.ui.view.dialogs.DialogView;

@RequiredArgsConstructor
public class ProductCreateDialogPresenter implements DialogPresenter {
    private final DialogView<GoodsResponse, GoodsRequest> view;
    private final GoodsService goodsService;

    @Override
    public void onSave() {
        try {
            GoodsResponse goodsResponse = goodsService.createGoods(view.getData());
            view.showSuccess("Товар " + goodsResponse.name() + " успешно создан!");
            view.closeDialog();
        } catch (RuntimeException ex) {
            view.showError(ex.getMessage());
        }
    }

    @Override
    public void initialize() {
        view.setData(null);
        view.setTitle("Создание товара");
        view.setSaveButtonText("Создать");
        view.showDialog();
    }
}
