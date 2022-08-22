package com.polyglot.utils.validator;

import lombok.AllArgsConstructor;

import java.util.function.Predicate;

@AllArgsConstructor
public class ValidationRule<T> implements Validable<T> {

    private final Predicate<T> predicate;

    private final String errorMessage;

    @Override
    public void validate(T t) {
        if (!predicate.test(t)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    public void validate(T... t) {
        for (T current : t) {
            validate(current);
        }
    }
}
