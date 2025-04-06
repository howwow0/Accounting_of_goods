package org.how_wow.infrastructure.ui.view.custom;

import org.how_wow.application.dto.goods.response.GoodsResponse;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Модель таблицы для отображения списка товаров.
 * Наследуется от AbstractTableModel для реализации пользовательской модели таблицы.
 */
public class ProductTableModel extends AbstractTableModel {

    private final List<GoodsResponse> goodsList;
    private final String[] columnNames = {"ID", "Название", "Категория", "Цена", "Кол-во", "Общая стоимость"};

    public ProductTableModel(List<GoodsResponse> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public int getRowCount() {
        return goodsList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GoodsResponse goods = goodsList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> goods.id();
            case 1 -> goods.name();
            case 2 -> goods.category();
            case 3 -> goods.price();
            case 4 -> goods.quantity();
            case 5 -> goods.totalCost();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setGoodsList(List<GoodsResponse> goodsList) {
        this.goodsList.clear();
        this.goodsList.addAll(goodsList);
        fireTableDataChanged();
    }

    public GoodsResponse getItemAt(int row) {
        if (row >= 0 && row < goodsList.size()) {
            return goodsList.get(row);
        }
        return null;
    }
}
