package org.how_wow.infrastructure.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProductDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<String> categoryCombo;
    private JFormattedTextField priceField;
    private JSpinner quantitySpinner;
    private JButton actionButton, cancelButton;

    public ProductDialog(JFrame parent, String title, String actionButtonText) {
        super(parent, title, true);
        setSize(400, 300);
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
        categoryCombo = new JComboBox<>(new String[]{"Электроника", "Одежда", "Продукты"});
        mainPanel.add(categoryCombo, gbc);

        // Цена
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Цена:"), gbc);

        gbc.gridx = 1;
        priceField = new JFormattedTextField();
        priceField.setColumns(10);
        mainPanel.add(priceField, gbc);

        // Количество
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Количество:"), gbc);

        gbc.gridx = 1;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        mainPanel.add(quantitySpinner, gbc);

        // Кнопки
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        actionButton = new JButton(actionButtonText);
        cancelButton = new JButton("Отмена");

        buttonPanel.add(actionButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        // Закрытие окна по кнопке "Отмена"
        cancelButton.addActionListener(e -> dispose());
    }

    public JButton getActionButton() {
        return actionButton;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JComboBox<String> getCategoryCombo() {
        return categoryCombo;
    }

    public JFormattedTextField getPriceField() {
        return priceField;
    }

    public JSpinner getQuantitySpinner() {
        return quantitySpinner;
    }
}
