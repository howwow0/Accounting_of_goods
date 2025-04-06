package org.how_wow.infrastructure.ui.presenters.dialogs.impl;

import lombok.RequiredArgsConstructor;
import org.how_wow.application.dto.stockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;
import org.how_wow.application.services.StockOperationService;
import org.how_wow.infrastructure.ui.presenters.dialogs.DialogPresenter;
import org.how_wow.infrastructure.ui.view.dialogs.DialogView;

@RequiredArgsConstructor
public class StockOperationsCreateDialogPresenter implements DialogPresenter {

    private final DialogView<StockOperationsResponse, StockOperationsRequest> view;
    private final StockOperationService stockOperationService;

    @Override
    public void onSave() {
        try {
            StockOperationsResponse stockOperationsResponse = stockOperationService.createStock(view.getData());
            view.showSuccess("Операция для ID товара " + stockOperationsResponse.goodsId() + " успешно создан!");
            view.closeDialog();
        } catch (RuntimeException ex) {
            view.showError(ex.getMessage());
        }
    }

    @Override
    public void initialize() {
        view.setTitle("Создание товара");
        view.setSaveButtonText("Создать");
        view.showDialog();
    }
}
