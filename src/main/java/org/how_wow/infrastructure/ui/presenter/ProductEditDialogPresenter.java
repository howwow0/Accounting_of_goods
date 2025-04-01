package org.how_wow.infrastructure.ui.presenter;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.view.ProductDialog;

import javax.swing.*;

@RequiredArgsConstructor
public class ProductEditDialogPresenter {
    private final ProductDialog view;
    private final GoodsService goodsService;

    public void bindOnEdit(Long productId) {
        try {
            GoodsResponse goodsResponse = goodsService.getGoodsById(productId);
            view.setNameFieldText(goodsResponse.name());
            view.setCategoryFieldText(goodsResponse.category());
            view.setPriceFieldValue(goodsResponse.price());
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(view.getComponent(), e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            view.close();
        }

        view.setActionButtonAction(e -> {
            GoodsRequest goodsRequest = GoodsRequest.builder()
                    .name(view.getNameFieldText())
                    .category(view.getCategoryFieldText())
                    .price(view.getPriceFieldValue())
                    .build();
            try {
                GoodsResponse goodsResponse = goodsService.updateGoods(productId, goodsRequest);
                JOptionPane.showMessageDialog(view.getComponent(), "Товар - {" + goodsResponse.name() + "} успешно обновлен", "Товар обновлен", JOptionPane.INFORMATION_MESSAGE);
                view.close();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(view.getComponent(), ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
