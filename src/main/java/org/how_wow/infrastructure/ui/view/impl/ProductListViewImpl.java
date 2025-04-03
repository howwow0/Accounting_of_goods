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
    private JTextField nameField;
    private JTextField categoryField;
    private JTable productsTable;
    private JButton applyFilterButton, resetFilterButton, refreshButton, addButton, editButton, deleteButton;
    private JSpinner pageSpinner;
    private JComboBox<Integer> pageSizeCombo;
    private ProductTableModel productTableModel;
    private JProgressBar progressBar;

    public ProductListViewImpl() {
        setTitle("Учет товаров - Список");
        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(createFilterPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel controlPanel = createControlPanel();
        controlPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        bottomPanel.add(controlPanel);

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setIndeterminate(true);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        bottomPanel.add(progressBar);

        return bottomPanel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Фильтры"));

        panel.add(new JLabel("Наименование:"));
        nameField = new JTextField(20);
        panel.add(nameField);

        panel.add(new JLabel("Категория:"));
        categoryField = new JTextField(20);
        panel.add(categoryField);

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
        productsTable.getSelectionModel().addListSelectionListener(_ -> {
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
    public String getNameFieldText() {
        return nameField.getText();
    }

    @Override
    public String getCategoryFieldText() {
        return categoryField.getText();
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
        nameField.setText("");
        categoryField.setText("");
    }

    @Override
    public long getSelectedProductId() {
        if (productsTable.getSelectedRow() == -1) {
            return -1;
        }
        return (long) productsTable.getValueAt(productsTable.getSelectedRow(), 0);
    }

    @Override
    public void setLoading(boolean isLoading) {
        progressBar.setVisible(isLoading);
        applyFilterButton.setEnabled(!isLoading);
        refreshButton.setEnabled(!isLoading);
        editButton.setEnabled(!isLoading && productsTable.getSelectedRow() != -1);
        deleteButton.setEnabled(!isLoading && productsTable.getSelectedRow() != -1);
        addButton.setEnabled(!isLoading);
        resetFilterButton.setEnabled(!isLoading);
    }
}
