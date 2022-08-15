package com.polyglot.utils.validator;

import java.util.function.Predicate;

public class ValidationRule<T> implements Validable<T> {

    private final Predicate<T> predicate;

    private final String errorMessage;

    public ValidationRule(Predicate<T> predicate, String errorMessage) {
        this.predicate = predicate;
        this.errorMessage = errorMessage;
    }

    @Override public void validate(T t) {
        if (!predicate.test(t)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
