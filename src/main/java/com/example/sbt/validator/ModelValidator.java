package com.example.sbt.validator;

import com.example.sbt.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class ModelValidator<T> {

    @Autowired
    private Validator validator;

    public void validateObject(T object, String errorMessage) {
        var validate = validator.validateObject(object);
        if (validate.hasErrors()) {
            final var errorsMessages = validate.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            throw new ValidationException(errorMessage, errorsMessages);
        }
    }
}
