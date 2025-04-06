package org.how_wow.infrastructure.ui.view.impl;

import lombok.Setter;
import org.how_wow.application.dto.goods.request.FilterGoodsRequest;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.infrastructure.ui.presenters.ProductListViewPresenter;
import org.how_wow.infrastructure.ui.view.ProductListView;
import org.how_wow.infrastructure.ui.view.custom.ProductTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ProductListViewImpl extends JFrame implements ProductListView {
    private final JTextField nameField = new JTextField(20);
    private final JTextField categoryField = new JTextField(20);
    private final JTable productsTable;
    private final ProductTableModel productTableModel;
    private final JButton applyFilterButton = new JButton("Применить");
    private final JButton resetFilterButton = new JButton("Сбросить");
    private final JButton refreshButton = new JButton("Обновить");
    private final JButton addButton = new JButton("Новый товар");
    private final JButton editButton = new JButton("Редактировать");
    private final JButton deleteButton = new JButton("Удалить");
    private final JButton operationsButton = new JButton("Операции");
    private final JSpinner pageSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    private final JComboBox<Integer> pageSizeCombo = new JComboBox<>(new Integer[]{10, 25, 50, 100});
    @Setter
    private ProductListViewPresenter presenter;

    public ProductListViewImpl() {
        setTitle("Учет товаров - Список");
        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        productTableModel = new ProductTableModel(new ArrayList<>());
        productsTable = new JTable(productTableModel);

        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(createFilterPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        setupListeners();
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Фильтры"));

        panel.add(new JLabel("Наименование:"));
        panel.add(nameField);

        panel.add(new JLabel("Категория:"));
        panel.add(categoryField);

        panel.add(applyFilterButton);
        panel.add(resetFilterButton);

        return panel;
    }

    private JScrollPane createTablePanel() {
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productsTable.setAutoCreateRowSorter(true);
        return new JScrollPane(productsTable);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paginationPanel.add(new JLabel("Страница:"));
        paginationPanel.add(pageSpinner);

        paginationPanel.add(new JLabel("Размер страницы:"));
        pageSizeCombo.setSelectedIndex(1);
        paginationPanel.add(pageSizeCombo);

        paginationPanel.add(refreshButton);
        paginationPanel.add(addButton);
        paginationPanel.add(editButton);
        paginationPanel.add(deleteButton);
        paginationPanel.add(operationsButton);

        panel.add(paginationPanel);

        return panel;
    }

    private void setupListeners() {
        productsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                presenter.onItemSelected(getSelectedItem());
            }
        });

        applyFilterButton.addActionListener(e -> presenter.onApplyFilters());
        resetFilterButton.addActionListener(e -> presenter.onResetFilters());
        refreshButton.addActionListener(e -> presenter.loadProducts(getCurrentPage(), getPageSize()));
        addButton.addActionListener(e -> presenter.onAddProduct());
        editButton.addActionListener(e -> presenter.onEditProduct());
        deleteButton.addActionListener(e -> presenter.onDeleteProduct());
        operationsButton.addActionListener(e -> presenter.onShowOperations());
    }

    @Override
    public void setOperationsButtonEnabled(boolean enabled) {
        editButton.setEnabled(enabled && getSelectedItem() != null);
        deleteButton.setEnabled(enabled && getSelectedItem() != null);
        operationsButton.setEnabled(enabled && getSelectedItem() != null);
    }

    @Override
    public void displayItems(List<GoodsResponse> items) {
        productTableModel.setGoodsList(items);
        productTableModel.fireTableDataChanged();
    }

    @Override
    public GoodsResponse getSelectedItem() {
        int row = productsTable.getSelectedRow();
        return row == -1 ? null : productTableModel.getItemAt(row);
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public boolean showWarningDelete(String message) {
        return JOptionPane.showConfirmDialog(
                this,
                message,
                "Подтверждение удаления",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION;
    }

    @Override
    public FilterGoodsRequest getFilters() {
        return FilterGoodsRequest.builder()
                .name(nameField.getText())
                .category(categoryField.getText())
                .build();
    }

    @Override
    public void resetFilters() {
        nameField.setText("");
        categoryField.setText("");
    }

    @Override
    public int getCurrentPage() {
        return (Integer) pageSpinner.getValue();
    }

    @Override
    public int getPageSize() {
        return (Integer) Objects.requireNonNull(pageSizeCombo.getSelectedItem());
    }

    @Override
    public void showView() {
        setVisible(true);
    }

}