package org.how_wow.infrastructure.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ExportDialog extends JDialog {
    private JCheckBox exportIdCheck, exportNameCheck, exportCategoryCheck, exportPriceCheck, exportQuantityCheck;
    private JButton exportButton, cancelButton;

    public ExportDialog(JFrame parent) {
        super(parent, "Экспорт данных", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Панель параметров экспорта
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Параметры экспорта"));

        exportIdCheck = new JCheckBox("Экспортировать ID", true);
        exportNameCheck = new JCheckBox("Экспортировать название", true);
        exportCategoryCheck = new JCheckBox("Экспортировать категорию", true);
        exportPriceCheck = new JCheckBox("Экспортировать цену", true);
        exportQuantityCheck = new JCheckBox("Экспортировать количество", true);

        optionsPanel.add(exportIdCheck);
        optionsPanel.add(exportNameCheck);
        optionsPanel.add(exportCategoryCheck);
        optionsPanel.add(exportPriceCheck);
        optionsPanel.add(exportQuantityCheck);

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

    public JCheckBox getExportNameCheck() {
        return exportNameCheck;
    }

    public JCheckBox getExportCategoryCheck() {
        return exportCategoryCheck;
    }

    public JCheckBox getExportPriceCheck() {
        return exportPriceCheck;
    }

    public JCheckBox getExportQuantityCheck() {
        return exportQuantityCheck;
    }
}
