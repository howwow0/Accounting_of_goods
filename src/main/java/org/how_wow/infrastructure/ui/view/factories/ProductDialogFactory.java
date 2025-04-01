package org.how_wow.infrastructure.ui.view.factories;

import org.how_wow.infrastructure.ui.view.impl.ProductDialogImpl;

public interface ProductDialogFactory {
    ProductDialogImpl createCreateDialog();
    ProductDialogImpl createEditDialog(long productId);
}