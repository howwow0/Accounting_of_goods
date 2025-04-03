package org.how_wow.application.validators;

import org.how_wow.exceptions.ValidationException;

/**
 * Валидатор для категории.
 */
public class CategoryValidator implements Validator<String> {

    /**
     * Проверяет категорию на валидность.
     *
     * @param s категория
     * @throws ValidationException если категория не валидна
     */
    @Override
    public void validate(String s) {
        if (s == null || s.isEmpty()) {
            throw new ValidationException("Категория не может быть пустой");
        }
        int MAX_LENGTH = 25;
        if (s.length() > MAX_LENGTH) {
            throw new ValidationException("Категория не может быть длиннее " + MAX_LENGTH + " символов");
        }
    }
}
