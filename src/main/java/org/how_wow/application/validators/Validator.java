package org.how_wow.application.validators;


/**
 * Валидатор
 */
public interface Validator<T> {
     /**
      * Проверка объекта
      *
      * @param t объект
      */
     void validate(T t);
}
