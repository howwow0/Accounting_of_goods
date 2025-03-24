package org.how_wow.application.validators;

import org.how_wow.domain.enums.OperationType;
import org.how_wow.exceptions.ValidationException;

/*
 * Валидатор для типа операции
 */
public class OperationTypeValidator implements Validator<OperationType> {

    /*
     * Проверяет, что тип операции не null
     */
    @Override
    public void validate(OperationType operationType) {
        if (operationType == null) {
            throw new ValidationException("Тип операции не может быть null");
        }
    }
}
