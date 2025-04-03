package org.how_wow.infrastructure.ui.view;

import org.how_wow.application.dto.goods.response.GoodsResponse;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public interface ProductListView {

    Component getComponent();

    String getNameFieldText();

    String getCategoryFieldText();

    long getCurrentPage();

    long getPageSize();

    void setEditButtonAction(ActionListener listener);

    void setAddButtonAction(ActionListener listener);

    void setRefreshButtonAction(ActionListener listener);

    void setResetFilterButtonAction(ActionListener listener);

    void setDeleteButtonAction(ActionListener listener);

    void setApplyFilterButtonAction(ActionListener listener);

    void setProductList(List<GoodsResponse> products);

    void clearFilterFields();

    long getSelectedProductId();

    void setLoading(boolean isLoading);
}