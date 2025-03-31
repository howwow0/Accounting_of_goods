package org.how_wow.infrastructure.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StockOperationsExportDialog extends JDialog {
    private JCheckBox exportIdCheck, exportGoodsIdCheck, exportOperationTypeCheck, exportQuantityCheck, exportDateCheck;
    private JButton exportButton, cancelButton;

    public StockOperationsExportDialog(JFrame parent) {
        super(parent, "Экспорт складских операций", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Панель параметров экспорта
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Параметры экспорта"));

        exportIdCheck = new JCheckBox("Экспортировать ID", true);
        exportGoodsIdCheck = new JCheckBox("Экспортировать ID товара", true);
        exportOperationTypeCheck = new JCheckBox("Экспортировать тип операции", true);
        exportQuantityCheck = new JCheckBox("Экспортировать количество", true);
        exportDateCheck = new JCheckBox("Экспортировать дату операции", true);

        optionsPanel.add(exportIdCheck);
        optionsPanel.add(exportGoodsIdCheck);
        optionsPanel.add(exportOperationTypeCheck);
        optionsPanel.add(exportQuantityCheck);
        optionsPanel.add(exportDateCheck);

        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        exportButton = new JButton("Экспорт");
        cancelButton = new JButton("Отмена");

        buttonPanel.add(exportButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        cancelButton.addActionListener(e -> dispose());
    }

    public JButton getExportButton() {
        return exportButton;
    }

    public JCheckBox getExportIdCheck() {
        return exportIdCheck;
    }

    public JCheckBox getExportGoodsIdCheck() {
        return exportGoodsIdCheck;
    }

    public JCheckBox getExportOperationTypeCheck() {
        return exportOperationTypeCheck;
    }

    public JCheckBox getExportQuantityCheck() {
        return exportQuantityCheck;
    }

    public JCheckBox getExportDateCheck() {
        return exportDateCheck;
    }
}
