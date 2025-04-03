package org.how_wow.application.validators;

import org.how_wow.domain.enums.OperationType;
import org.how_wow.exceptions.ValidationException;

/**
 * Валидатор для типа операции
 */
public class OperationTypeValidator implements Validator<OperationType> {

 /**
     * Проверяет тип операции на корректность.
     *
     * @param operationType тип операции
     * @throws ValidationException если тип операции null
     */
    @Override
    public void validate(OperationType operationType) {
        if (operationType == null) {
            throw new ValidationException("Тип операции не может быть null");
        }
    }
}
