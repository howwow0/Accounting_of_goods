package org.how_wow.infrastructure.ui.view.dialogs.impl;

import lombok.Setter;
import org.how_wow.application.dto.goods.request.GoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.infrastructure.ui.presenters.dialogs.DialogPresenter;
import org.how_wow.infrastructure.ui.view.custom.BigDecimalTextField;
import org.how_wow.infrastructure.ui.view.dialogs.DialogView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;

public class ProductDialog extends JDialog implements DialogView<GoodsResponse, GoodsRequest> {
    private final JTextField nameField;
    private final JTextField categoryField;
    private final BigDecimalTextField priceField;
    private final JButton actionButton;

    @Setter
    private DialogPresenter presenter;

    public ProductDialog(Frame parent) {
        super(parent, ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        setMaximumSize(new Dimension(400, 300));
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_END;

        // Название
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Название:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        mainPanel.add(nameField, gbc);

        // Категория
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Категория:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        categoryField = new JTextField(20);
        mainPanel.add(categoryField, gbc);

        // Цена
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Цена:"), gbc);

        gbc.gridx = 1;
        priceField = new BigDecimalTextField();
        priceField.setColumns(10);
        mainPanel.add(priceField, gbc);


        // Кнопки
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        actionButton = new JButton();
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(actionButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    @Override
    public void setData(GoodsResponse data) {
        if (data != null) {
            nameField.setText(data.name());
            categoryField.setText(data.category());
            priceField.setValue(data.price());
        } else {
            nameField.setText("");
            categoryField.setText("");
            priceField.setValue(BigDecimal.ZERO);
        }
    }

    @Override
    public GoodsRequest getData() {
        return GoodsRequest.builder()
                .name(nameField.getText())
                .category(categoryField.getText())
                .price(priceField.getBigDecimalValue())
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
