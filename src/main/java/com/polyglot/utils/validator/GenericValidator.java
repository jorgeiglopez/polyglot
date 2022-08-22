package com.polyglot.utils.validator;

import java.util.LinkedList;
import java.util.List;

public class GenericValidator<T> implements Validable<T> {

    private final List<Validable<T>> rules = new LinkedList<>();

    public GenericValidator(List<Validable<T>> rules) {
        this.rules.addAll(rules);
    }

    public GenericValidator(Validators... rules) {
        for (Validators item : rules) {
            try {
                this.rules.add((Validable<T>) item.getRule());
            } catch (ClassCastException ignored) {
            }
        }
    }

    public static <T> GenericValidator<T> of(Validators... rules) {
        return new GenericValidator<>(rules);
    }

    @Override
    public void validate(final T toValidate) {
        rules.forEach(rule -> rule.validate(toValidate));
    }

    @Override
    public void validate(T... t) {
        for (T current : t) {
            validate(current);
        }
    }
}
