package org.how_wow.infrastructure.ui.view.factories;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.services.GoodsService;
import org.how_wow.infrastructure.ui.presenter.ProductCreateDialogPresenter;
import org.how_wow.infrastructure.ui.presenter.ProductEditDialogPresenter;
import org.how_wow.infrastructure.ui.view.impl.ProductDialogImpl;

import javax.swing.*;

@RequiredArgsConstructor
public class SwingProductDialogFactory implements ProductDialogFactory {
    private final JFrame parentFrame;
    private final GoodsService goodsService;

    @Override
    public ProductDialogImpl createCreateDialog() {
        ProductDialogImpl dialog = new ProductDialogImpl(parentFrame, "Создание товара", "Создать");
        new ProductCreateDialogPresenter(dialog, goodsService).bindOnCreate();
        return dialog;
    }

    @Override
    public ProductDialogImpl createEditDialog(long productId) {
        ProductDialogImpl dialog = new ProductDialogImpl(parentFrame, "Редактирование товара", "Сохранить");
        new ProductEditDialogPresenter(dialog, goodsService).bindOnEdit(productId);
        return dialog;
    }
}