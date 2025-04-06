package org.how_wow.infrastructure.ui.view.factories;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.presenters.dialogs.DialogPresenter;
import org.how_wow.infrastructure.ui.presenters.dialogs.impl.ProductCreateDialogPresenter;
import org.how_wow.infrastructure.ui.presenters.dialogs.impl.ProductEditDialogPresenter;
import org.how_wow.infrastructure.ui.view.dialogs.DialogView;
import org.how_wow.infrastructure.ui.view.dialogs.impl.ProductDialog;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
public class ProductDialogFactory {
    private final Frame parent;

    public void openProductCreateDialog(GoodsService goodsService) {
        SwingUtilities.invokeLater(() -> {
            DialogView<GoodsResponse, GoodsRequest> view = new ProductDialog(parent);
            DialogPresenter presenter = new ProductCreateDialogPresenter(view, goodsService);
            view.setPresenter(presenter);
            presenter.initialize();
        });
    }

    public void openProductEditDialog(Long productId, GoodsService goodsService) {
        SwingUtilities.invokeLater(() -> {
            DialogView<GoodsResponse, GoodsRequest> view = new ProductDialog(parent);
            DialogPresenter presenter = new ProductEditDialogPresenter(view, goodsService, productId);
            view.setPresenter(presenter);
            presenter.initialize();
        });
    }
}
