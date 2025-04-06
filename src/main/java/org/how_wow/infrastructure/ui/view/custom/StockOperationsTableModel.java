package org.how_wow.infrastructure.ui.view.custom;

import org.how_wow.application.dto.stockOperations.response.StockOperationsResponse;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Класс модели таблицы для отображения операций со складом.
 * Наследуется от AbstractTableModel для реализации пользовательской модели таблицы.
 */
public class StockOperationsTableModel extends AbstractTableModel {

    private final List<StockOperationsResponse> stockOperationsResponseList;
    private final String[] columnNames = {"ID", "ID Товара", "Тип операции", "Кол-во", "Время операции"};

    public StockOperationsTableModel(List<StockOperationsResponse> stockOperationsResponseList) {
        this.stockOperationsResponseList = stockOperationsResponseList;
    }

    @Override
    public int getRowCount() {
        return stockOperationsResponseList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StockOperationsResponse goods = stockOperationsResponseList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> goods.id();
            case 1 -> goods.goodsId();
            case 2 -> goods.operationType();
            case 3 -> goods.quantity();
            case 4 -> goods.operationDateTime();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setStockOperationsResponseList(List<StockOperationsResponse> stockOperationsResponseList) {
        this.stockOperationsResponseList.clear();
        this.stockOperationsResponseList.addAll(stockOperationsResponseList);
        fireTableDataChanged();
    }

    public StockOperationsResponse getItemAt(int row) {
        if (row >= 0 && row < stockOperationsResponseList.size()) {
            return stockOperationsResponseList.get(row);
        }
        return null;
    }
}
