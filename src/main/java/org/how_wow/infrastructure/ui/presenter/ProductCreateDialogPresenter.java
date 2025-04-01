package org.how_wow.infrastructure.ui.presenter;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.view.ProductDialog;

import javax.swing.*;

@RequiredArgsConstructor
public class ProductCreateDialogPresenter {

    private final ProductDialog view;
    private final GoodsService goodsService;

    public void bindOnCreate() {
        view.setActionButtonAction(e -> {
            GoodsRequest goodsRequest = GoodsRequest.builder()
                    .name(view.getNameFieldText())
                    .category(view.getCategoryFieldText())
                    .price(view.getPriceFieldValue())
                    .build();

            try {
                GoodsResponse goodsResponse = goodsService.createGoods(goodsRequest);
                JOptionPane.showMessageDialog(view.getComponent(), "Товар - {" + goodsResponse.name() + "} успешно создан", "Товар создан", JOptionPane.INFORMATION_MESSAGE);
                view.close();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(view.getComponent(), ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
