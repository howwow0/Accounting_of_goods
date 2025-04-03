package org.how_wow.application.validators;

import org.how_wow.exceptions.ValidationException;

/**
 * Валидатор для ID
 */
public class LongIdValidator implements Validator<Long> {

    /**
     * Проверка ID
     *
     * @param aLong ID
     * @throws ValidationException если ID пустой или меньше или равен нулю
     */
    @Override
    public void validate(Long aLong) {
        if (aLong == null)
            throw new ValidationException("ID не может быть пустым");
        if (aLong <= 0)
            throw new ValidationException("ID должен быть больше нуля");
    }
}
