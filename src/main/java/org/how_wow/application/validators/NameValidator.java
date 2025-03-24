package org.how_wow.application.validators;

import org.how_wow.exceptions.ValidationException;

/**
 * Валидатор для проверки наименования товара.
 */
public class NameValidator implements Validator<String> {

    private final int MAX_LENGTH = 50;
    /**
     * Проверяет наименование товара на корректность.
     *
     * @throws ValidationException если наименование товара пустое или длиннее 100 символов
     * @param s наименование товара
     */
    @Override
    public void validate(String s) {
        if(s == null || s.isBlank()) {
            throw new ValidationException("Наименование товара не может быть пустым");
        }
        if(s.length() > MAX_LENGTH) {
            throw new ValidationException("Наименование товара не может быть длиннее " + MAX_LENGTH + " символов");
        }
    }
}
