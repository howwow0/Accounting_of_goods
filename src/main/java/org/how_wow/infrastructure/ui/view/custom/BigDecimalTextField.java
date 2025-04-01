package org.how_wow.infrastructure.ui.view.custom;

import javax.swing.*;
import javax.swing.text.*;
import java.math.BigDecimal;
import java.text.*;

public class BigDecimalTextField extends JFormattedTextField {
    private int decimalPlaces = 2;

    public BigDecimalTextField() {
        this(2);
    }

    public BigDecimalTextField(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        setFormatterFactory(createFormatterFactory());
        setValue(BigDecimal.ZERO);
        setColumns(20);
    }

    private AbstractFormatterFactory createFormatterFactory() {
        return new AbstractFormatterFactory() {
            @Override
            public AbstractFormatter getFormatter(JFormattedTextField tf) {
                NumberFormat format = NumberFormat.getNumberInstance();
                format.setMinimumFractionDigits(decimalPlaces);
                format.setMaximumFractionDigits(decimalPlaces);

                if (format instanceof DecimalFormat) {
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator(',');
                    ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
                }

                NumberFormatter formatter = new NumberFormatter(format);
                formatter.setValueClass(BigDecimal.class);
                formatter.setAllowsInvalid(false);
                formatter.setCommitsOnValidEdit(true);
                return formatter;
            }
        };
    }

    public BigDecimal getBigDecimalValue() {
        return (BigDecimal) getValue();
    }

    public void setBigDecimalValue(BigDecimal value) {
        setValue(value);
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        setFormatterFactory(createFormatterFactory());
    }
}