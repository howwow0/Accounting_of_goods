package org.how_wow.application.validators;

import org.how_wow.exceptions.ValidationException;

import java.math.BigDecimal;

/**
 * Валидатор для проверки цены товара.
 */
public class PriceValidator implements Validator<BigDecimal> {

    /**
     * Проверяет цену товара на корректность.
     *
     * @param bigDecimal цена товара
     * @throws ValidationException если цена товара меньше 0 или равна 0
     */
    @Override
    public void validate(BigDecimal bigDecimal) {
        if (bigDecimal == null)
            throw new ValidationException("Цена товара не может быть пустой");

        if (bigDecimal.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Цена товара не может быть 0 и меньше 0");
    }
}
