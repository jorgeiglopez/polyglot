package com.polyglot.utils.validator;

import java.util.LinkedList;
import java.util.List;

public abstract class GenericValidator<T> implements ValidationRule<T> {

    private final List<ValidationRule<T>> rules = new LinkedList<>();

    public GenericValidator(List<ValidationRule<T>> rules) {
        this.rules.addAll(rules);
    }

    //    public GenericValidator(ValidationRule rule...) {
    //
    //    }

    @Override
    public void validate(final T toValidate) {
        rules.forEach(rule -> rule.validate(toValidate));
    }
}
