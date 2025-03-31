package org.how_wow.infrastructure.ui.view;

import org.how_wow.application.dto.goods.response.GoodsResponse;
import javax.swing.table.AbstractTableModel;
import java.util.List;

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
        switch (columnIndex) {
            case 0:
                return goods.id();
            case 1:
                return goods.name();
            case 2:
                return goods.category();
            case 3:
                return goods.price();
            case 4:
                return goods.quantity();
            case 5:
                return goods.totalCost();
            default:
                return null;
        }
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
}
