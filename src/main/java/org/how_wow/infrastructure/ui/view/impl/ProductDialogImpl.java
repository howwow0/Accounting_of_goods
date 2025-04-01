package org.how_wow.infrastructure.ui.view.impl;

import lombok.Getter;
import org.how_wow.infrastructure.ui.view.ProductDialog;
import org.how_wow.infrastructure.ui.view.custom.BigDecimalTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class ProductDialogImpl extends JDialog implements ProductDialog {
    private final JTextField nameField;
    private final JTextField categoryField;
    private final BigDecimalTextField priceField;
    private final JButton actionButton;

    public ProductDialogImpl(JFrame parent, String title, String actionButtonText) {
        super(parent, title, true);
        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        setMaximumSize(new Dimension(400, 300));
        setLocationRelativeTo(parent);

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
        actionButton = new JButton(actionButtonText);
        JButton cancelButton = new JButton("Отмена");

        buttonPanel.add(actionButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        // Закрытие окна по кнопке "Отмена"
        cancelButton.addActionListener(e -> dispose());
    }

    @Override
    public void setActionButtonAction(ActionListener listener) {
        actionButton.addActionListener(listener);
    }

    @Override
    public String getNameFieldText() {
        return nameField.getText();
    }

    @Override
    public String getCategoryFieldText() {
        return categoryField.getText();
    }

    @Override
    public BigDecimal getPriceFieldValue() {
        return priceField.getBigDecimalValue();
    }

    @Override
    public void setPriceFieldValue(BigDecimal price) {
        priceField.setBigDecimalValue(price);
    }

    @Override
    public void setNameFieldText(String name) {
        nameField.setText(name);
    }

    @Override
    public void setCategoryFieldText(String category) {
        categoryField.setText(category);
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
