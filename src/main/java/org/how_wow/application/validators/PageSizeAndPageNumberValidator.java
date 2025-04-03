package org.how_wow.application.validators;

import org.how_wow.exceptions.ValidationException;

/**
 * Валидатор для проверки размера страницы и номера страницы
 */
public class PageSizeAndPageNumberValidator implements Validator<Long> {
    /**
     * Проверяет, что размер страницы и номер страницы больше 0
     *
     * @param aLong размер страницы или номер страницы
     * @throws ValidationException если размер страницы или номер страницы меньше 0 или равен 0
     */
    @Override
    public void validate(Long aLong) {
        if (aLong == null)
            throw new ValidationException("Размер страницы или номер страницы не может быть null");
        if (aLong <= 0) {
            throw new ValidationException("Размер страницы или номер страницы должен быть больше 0");
        }

    }
}
