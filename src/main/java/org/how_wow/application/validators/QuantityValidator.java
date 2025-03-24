package org.how_wow.application.validators;

import org.how_wow.exceptions.ValidationException;

/**
 * Валидатор для проверки количества товара.
 */
public class QuantityValidator implements Validator<Long> {
    /**
     * Проверяет количество товара на корректность.
     *
     * @param aLong количество товара
     * @throws ValidationException если количество товара меньше 0 или равно 0
     */
    @Override
    public void validate(Long aLong) {
        if (aLong == null)
            throw new ValidationException("Количество товара не может быть пустым");
        if (aLong <= 0)
            throw new ValidationException("Количество товара не может быть 0 и меньше 0");
    }
}
