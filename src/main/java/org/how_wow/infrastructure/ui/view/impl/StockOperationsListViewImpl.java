package org.how_wow.infrastructure.ui.view.impl;

import lombok.Setter;
import org.how_wow.application.dto.stockOperations.request.FilterStockOperationsRequest;
import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;
import org.how_wow.application.dto.goods.response.GoodsResponse;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.infrastructure.ui.presenters.StockOperationsListViewPresenter;
import org.how_wow.infrastructure.ui.view.StockOperationsListView;
import org.how_wow.infrastructure.ui.view.custom.StockOperationsTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class StockOperationsListViewImpl extends JDialog implements StockOperationsListView {
    private JComboBox<String> operationTypeFilter;
    private JSpinner dateFromSpinner, dateToSpinner;
    private JTable operationsTable;
    private JButton applyFilterButton, resetFilterButton, refreshButton, addButton, deleteButton, deleteAllButton;
    private JSpinner pageSpinner;
    private JComboBox<Integer> pageSizeCombo;
    private StockOperationsTableModel operationsTableModel;
    private JLabel productNameLabel, categoryLabel, quantityLabel, priceLabel, totalCostLabel;

    @Setter
    private StockOperationsListViewPresenter presenter;

    public StockOperationsListViewImpl(Frame parent) {
        super(parent, ModalityType.APPLICATION_MODAL);
        setTitle("Учет товаров - Операции: ");
        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        topContainer.add(createProductInfoPanel());
        topContainer.add(createFilterPanel());


        mainPanel.add(topContainer, BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);

        add(mainPanel);

        applyFilterButton.addActionListener(e -> presenter.onApplyFilters());
        resetFilterButton.addActionListener(e -> presenter.onResetFilters());
        refreshButton.addActionListener(e -> presenter.loadStockOperations(getCurrentPage(), getPageSize()));
        addButton.addActionListener(e -> presenter.onAddStockOperations());
        deleteButton.addActionListener(e -> presenter.onDeleteStockOperations());
        deleteAllButton.addActionListener(e -> presenter.onDeleteAllStockOperations());
        operationsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                presenter.onItemSelected(getSelectedItem());
            }
        });
    }


    private JPanel createProductInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Информация о товаре"));

        productNameLabel = new JLabel("Наименование: ");
        categoryLabel = new JLabel("Категория: ");
        quantityLabel = new JLabel("Количество на складе: ");
        priceLabel = new JLabel("Цена: ");
        totalCostLabel = new JLabel("Общая стоимость: ");

        panel.add(productNameLabel);
        panel.add(categoryLabel);
        panel.add(quantityLabel);
        panel.add(priceLabel);
        panel.add(totalCostLabel);


        return panel;
    }


    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Фильтры операций"));

        panel.add(new JLabel("Тип операции:"));
        operationTypeFilter = new JComboBox<>(new String[]{"Все", "Поступление", "Списание"});
        panel.add(operationTypeFilter);

        panel.add(new JLabel("Дата с:"));
        dateFromSpinner = new JSpinner(new SpinnerDateModel());
        dateFromSpinner.setEditor(new JSpinner.DateEditor(dateFromSpinner, "dd.MM.yyyy"));
        panel.add(dateFromSpinner);

        panel.add(new JLabel("Дата по:"));
        dateToSpinner = new JSpinner(new SpinnerDateModel());
        dateToSpinner.setEditor(new JSpinner.DateEditor(dateToSpinner, "dd.MM.yyyy"));
        panel.add(dateToSpinner);

        applyFilterButton = new JButton("Применить");
        resetFilterButton = new JButton("Сбросить");
        panel.add(applyFilterButton);
        panel.add(resetFilterButton);

        return panel;
    }

    private JScrollPane createTablePanel() {
        operationsTableModel = new StockOperationsTableModel(new ArrayList<>());
        operationsTable = new JTable(operationsTableModel);
        operationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        operationsTable.setAutoCreateRowSorter(true);

        return new JScrollPane(operationsTable);
    }


    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("Страница:"));
        pageSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        panel.add(pageSpinner);

        panel.add(new JLabel("Размер страницы:"));
        pageSizeCombo = new JComboBox<>(new Integer[]{10, 25, 50, 100});
        pageSizeCombo.setSelectedIndex(1);
        panel.add(pageSizeCombo);

        refreshButton = new JButton("Обновить");
        addButton = new JButton("Новая операция");
        deleteButton = new JButton("Удалить");
        deleteAllButton = new JButton("Удалить все");
        panel.add(refreshButton);
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(deleteAllButton);

        return panel;
    }


    @Override
    public void displayItems(List<StockOperationsResponse> items) {
        operationsTableModel.setStockOperationsResponseList(items);
        operationsTableModel.fireTableDataChanged();
    }

    @Override
    public void setOperationsButtonEnabled(boolean enabled) {
        deleteButton.setEnabled(enabled && getSelectedItem() != null);
    }

    @Override
    public StockOperationsResponse getSelectedItem() {
        int row = operationsTable.getSelectedRow();
        return row == -1 ? null : operationsTableModel.getItemAt(row);
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
    public void resetFilters() {
        dateFromSpinner.setValue(new Date());
        dateToSpinner.setValue(new Date());
        operationTypeFilter.setSelectedIndex(0);
    }

    @Override
    public FilterStockOperationsRequest getFilters() {
        OperationType operationType = null;
        if (operationTypeFilter.getSelectedIndex() != 0) {
            operationType = OperationType.fromString(Objects.requireNonNull(operationTypeFilter.getSelectedItem()).toString());
        }

        Date date1 = (Date) dateFromSpinner.getValue();
        Date date2 = (Date) dateToSpinner.getValue();
        LocalDateTime startDateTime = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return FilterStockOperationsRequest.builder()
                .operationType(operationType)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
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

    @Override
    public void setProductInfo(GoodsResponse goods) {
        setTitle("Учет товаров - Операции: " + goods.name());
        productNameLabel.setText("Наименование: " + goods.name());
        categoryLabel.setText("Категория: " + goods.category());
        quantityLabel.setText("Количество на складе: " + goods.quantity());
        priceLabel.setText("Цена: " + goods.price());
        totalCostLabel.setText("Общая стоимость: " + goods.totalCost());
    }
}