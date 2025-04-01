package org.how_wow.infrastructure.ui.view.impl;

import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.infrastructure.ui.view.ProductListView;
import org.how_wow.infrastructure.ui.view.custom.ProductTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductListViewImpl extends JFrame implements ProductListView {
    private JTextField searchField;
    private JTextField categoryFilter;
    private JTable productsTable;
    private JButton applyFilterButton, resetFilterButton, refreshButton, addButton, editButton, deleteButton;
    private JSpinner pageSpinner;
    private JComboBox<Integer> pageSizeCombo;
    private ProductTableModel productTableModel;

    public ProductListViewImpl() {
        setTitle("Учет товаров - Список");
        setSize(850, 600);
        setMinimumSize(new Dimension(850, 600));
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
        categoryFilter = new JTextField(20);
        panel.add(categoryFilter);

        applyFilterButton = new JButton("Применить");
        resetFilterButton = new JButton("Сбросить");
        panel.add(applyFilterButton);
        panel.add(resetFilterButton);

        return panel;
    }

    private JScrollPane createTablePanel() {
        productTableModel = new ProductTableModel(new ArrayList<>());
        productsTable = new JTable(productTableModel);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productsTable.setAutoCreateRowSorter(true);
        productsTable.getSelectionModel().addListSelectionListener(x -> {
            if (productsTable.getSelectedRow() == -1) {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            } else {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        });

        return new JScrollPane(productsTable);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        panel.add(new JLabel("Страница:"));
        pageSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        panel.add(pageSpinner);

        panel.add(new JLabel("Размер страницы:"));
        pageSizeCombo = new JComboBox<>(new Integer[]{10, 25, 50, 100});
        pageSizeCombo.setSelectedIndex(1);
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

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public String getSearchField() {
        return searchField.getText();
    }

    @Override
    public String getCategoryFilter() {
        return categoryFilter.getText();
    }

    @Override
    public long getCurrentPage() {
        return Long.parseLong(String.valueOf(pageSpinner.getValue()));
    }

    @Override
    public long getPageSize() {
        return Long.parseLong(String.valueOf(Objects.requireNonNullElse(pageSizeCombo.getSelectedItem(), 1)));
    }

    @Override
    public void setEditButtonAction(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    @Override
    public void setAddButtonAction(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    @Override
    public void setRefreshButtonAction(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    @Override
    public void setResetFilterButtonAction(ActionListener listener) {
        resetFilterButton.addActionListener(listener);
    }

    @Override
    public void setDeleteButtonAction(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    @Override
    public void setApplyFilterButtonAction(ActionListener listener) {
        applyFilterButton.addActionListener(listener);
    }

    @Override
    public void setProductList(List<GoodsResponse> products) {
        productTableModel.setGoodsList(products);
    }

    @Override
    public void clearFilterFields() {
        searchField.setText("");
        categoryFilter.setText("");
    }

    @Override
    public long getSelectedProductId() {
        if (productsTable.getSelectedRow() == -1) {
            return -1;
        }
        return (long) productsTable.getValueAt(productsTable.getSelectedRow(), 0);
    }
}
