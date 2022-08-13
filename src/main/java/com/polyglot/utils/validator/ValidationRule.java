package com.polyglot.utils.validator;

public interface ValidationRule<T> {

    void validate(T t);

}
