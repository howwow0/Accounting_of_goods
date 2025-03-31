package org.how_wow.infrastructure.ui.view;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Getter
public class ProductListView extends JFrame {
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JTable productsTable;
    private JButton applyFilterButton, resetFilterButton, refreshButton, addButton, editButton, deleteButton;
    private JSpinner pageSpinner;
    private JComboBox<Integer> pageSizeCombo;

    public ProductListView() {
        setTitle("Учет товаров - Список");
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

        panel.add(new JLabel("Поиск:"));
        searchField = new JTextField(20);
        panel.add(searchField);

        panel.add(new JLabel("Категория:"));
        categoryFilter = new JComboBox<>(new String[]{"Все", "Электроника", "Одежда", "Продукты"});
        panel.add(categoryFilter);

        applyFilterButton = new JButton("Применить");
        resetFilterButton = new JButton("Сбросить");
        panel.add(applyFilterButton);
        panel.add(resetFilterButton);

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Название", "Категория", "Цена", "Кол-во", "Общая стоимость"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        productsTable = new JTable(tableModel);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productsTable.setAutoCreateRowSorter(true);

        return new JScrollPane(productsTable);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        panel.add(new JLabel("Страница:"));
        pageSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        panel.add(pageSpinner);

        panel.add(new JLabel("Размер страницы:"));
        pageSizeCombo = new JComboBox<>(new Integer[]{10, 25, 50, 100});
        pageSizeCombo.setSelectedIndex(2);
        panel.add(pageSizeCombo);

        refreshButton = new JButton("Обновить");
        addButton = new JButton("Новый товар");
        editButton = new JButton("Редактировать");
        deleteButton = new JButton("Удалить");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        panel.add(refreshButton);
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductListView productListView = new ProductListView();
            productListView.setVisible(true);

            ExportDialog exportDialog = new ExportDialog(productListView);
            exportDialog.setVisible(true);

            // Открыть диалог для добавления
            ProductDialog addDialog = new ProductDialog(productListView, "Добавление товара", "Добавить");
            addDialog.setVisible(true);

            // Открыть диалог для редактирования
            ProductDialog editDialog = new ProductDialog(productListView, "Редактирование товара", "Сохранить");
            editDialog.setVisible(true);

            ImportDialog importDialog = new ImportDialog(productListView);
            importDialog.setVisible(true);

            StockOperationsDialog stockOperationsDialog = new StockOperationsDialog(productListView, "Добавление товара на склад", "Добавить");
            stockOperationsDialog.setVisible(true);

            StockOperationsView stockOperationsView = new StockOperationsView();
            stockOperationsView.setVisible(true);
        });
    }
}
