package org.how_wow.infrastructure.ui.view.factories;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.stockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.infrastructure.ui.presenters.dialogs.DialogPresenter;
import org.how_wow.infrastructure.ui.presenters.dialogs.impl.StockOperationsCreateDialogPresenter;
import org.how_wow.infrastructure.ui.view.dialogs.DialogView;
import org.how_wow.infrastructure.ui.view.dialogs.impl.StockOperationsDialog;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
public class StockOperationsDialogFactory {
    private final Frame parent;

    public void openStockOperationsCreateDialog(Long goodsId, StockOperationService stockOperationsService) {
        SwingUtilities.invokeLater(() -> {
            DialogView<StockOperationsResponse, StockOperationsRequest> view = new StockOperationsDialog(parent, goodsId);
            DialogPresenter presenter = new StockOperationsCreateDialogPresenter(view, stockOperationsService);
            view.setPresenter(presenter);
            view.setData(StockOperationsResponse.builder()
                    .goodsId(goodsId)
                    .quantity(1L)
                    .operationType(OperationType.INBOUND)
                    .build());
            presenter.initialize();
        });
    }
}
