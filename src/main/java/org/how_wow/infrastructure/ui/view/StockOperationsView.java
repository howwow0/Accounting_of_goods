package org.how_wow.infrastructure.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StockOperationsView extends JFrame {
    private JTable operationsTable;
    private JComboBox<String> goodsFilter, operationTypeFilter;
    private JSpinner dateFromSpinner, dateToSpinner;
    private JButton applyFilterButton, resetFilterButton, addButton, editButton, deleteButton;

    public StockOperationsView() {
        setTitle("Учет товаров - Операции");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(createFilterPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Фильтры"));

        panel.add(new JLabel("Товар:"));
        goodsFilter = new JComboBox<>(new String[]{"Все", "Товар 1", "Товар 2"}); // Должно загружаться из БД
        panel.add(goodsFilter);

        panel.add(new JLabel("Тип операции:"));
        operationTypeFilter = new JComboBox<>(new String[]{"Все", "Поступление", "Списание"});
        panel.add(operationTypeFilter);

        panel.add(new JLabel("Дата с:"));
        dateFromSpinner = new JSpinner(new SpinnerDateModel());
        panel.add(dateFromSpinner);

        panel.add(new JLabel("Дата по:"));
        dateToSpinner = new JSpinner(new SpinnerDateModel());
        panel.add(dateToSpinner);

        applyFilterButton = new JButton("Применить");
        resetFilterButton = new JButton("Сбросить");
        panel.add(applyFilterButton);
        panel.add(resetFilterButton);

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Товар", "Тип операции", "Количество", "Дата"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        operationsTable = new JTable(tableModel);
        operationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        operationsTable.setAutoCreateRowSorter(true);

        return new JScrollPane(operationsTable);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        addButton = new JButton("Новая операция");
        editButton = new JButton("Редактировать");
        deleteButton = new JButton("Удалить");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StockOperationsView operationsView = new StockOperationsView();
            operationsView.setVisible(true);
        });
    }
}
