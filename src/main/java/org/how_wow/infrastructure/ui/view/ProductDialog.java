package org.how_wow.infrastructure.ui.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public interface ProductDialog {
    void setActionButtonAction(ActionListener listener);

    String getNameFieldText();

    String getCategoryFieldText();

    BigDecimal getPriceFieldValue();

    void setPriceFieldValue(BigDecimal price);

    void setNameFieldText(String name);

    void setCategoryFieldText(String category);

    void close();

    Component getComponent();
}