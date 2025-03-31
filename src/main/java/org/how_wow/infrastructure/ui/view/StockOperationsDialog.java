package org.how_wow.infrastructure.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import org.how_wow.domain.enums.OperationType;

public class StockOperationsDialog extends JDialog {
    private JComboBox<Long> goodsComboBox;
    private JComboBox<OperationType> operationTypeComboBox;
    private JSpinner quantitySpinner;
    private JButton actionButton, cancelButton;

    public StockOperationsDialog(JFrame parent, String title, String actionButtonText) {
        super(parent, title, true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_END;

        // Товар
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Товар:"), gbc);

        gbc.gridx = 1;
        goodsComboBox = new JComboBox<>();  // Добавьте здесь товары
        mainPanel.add(goodsComboBox, gbc);

        // Тип операции
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Тип операции:"), gbc);

        gbc.gridx = 1;
        operationTypeComboBox = new JComboBox<>(OperationType.values());
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

    public JComboBox<Long> getGoodsComboBox() {
        return goodsComboBox;
    }

    public JComboBox<OperationType> getOperationTypeComboBox() {
        return operationTypeComboBox;
    }

    public JSpinner getQuantitySpinner() {
        return quantitySpinner;
    }
}
