package org.how_wow.infrastructure.ui.view.dialogs.impl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

import lombok.Setter;
import org.how_wow.application.dto.stockOperations.request.StockOperationsRequest;
import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.infrastructure.ui.presenters.dialogs.DialogPresenter;
import org.how_wow.infrastructure.ui.view.dialogs.DialogView;

public class StockOperationsDialog extends JDialog implements DialogView<StockOperationsResponse, StockOperationsRequest> {
    private final JComboBox<String> operationTypeComboBox;
    private final JSpinner quantitySpinner;
    private final JButton actionButton;
    private final JLabel goodsIdLabel;
    private final Long goodsId;
    @Setter
    private DialogPresenter presenter;

    public StockOperationsDialog(Frame parent, Long goodsId) {
        super(parent, ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        setMaximumSize(new Dimension(400, 300));
        this.goodsId = goodsId;

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_END;

        // Товар
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("ID товара:"), gbc);
        gbc.gridx = 1;
        goodsIdLabel = new JLabel(goodsId.toString());
        mainPanel.add(goodsIdLabel, gbc);

        // Тип операции
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Тип операции:"), gbc);

        gbc.gridx = 1;
        operationTypeComboBox = new JComboBox<>(new String[]{"Поступление", "Списание"});
        mainPanel.add(operationTypeComboBox, gbc);

        // Количество
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Количество:"), gbc);

        gbc.gridx = 1;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        mainPanel.add(quantitySpinner, gbc);

        // Кнопки
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        actionButton = new JButton();
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(_ -> dispose());

        buttonPanel.add(actionButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

    }

    @Override
    public void setData(StockOperationsResponse data) {
        goodsIdLabel.setText(String.valueOf(data.goodsId()));
        quantitySpinner.setValue(data.quantity().intValue());
        operationTypeComboBox.setSelectedItem(OperationType.toString(data.operationType()));
    }

    @Override
    public StockOperationsRequest getData() {
        return StockOperationsRequest.builder()
                .goodsId(goodsId)
                .operationType(OperationType.fromString(Objects.requireNonNull(operationTypeComboBox.getSelectedItem()).toString()))
                .quantity(Long.valueOf((Integer) quantitySpinner.getValue()))
                .build();
    }

    @Override
    public void setSaveButtonText(String text) {
        actionButton.setText(text);
    }

    @Override
    public void showDialog() {
        actionButton.addActionListener(e -> presenter.onSave());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void closeDialog() {
        presenter = null;
        dispose();
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

}
