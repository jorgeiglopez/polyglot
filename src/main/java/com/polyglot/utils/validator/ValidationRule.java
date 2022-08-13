package com.polyglot.utils.validator;

public interface ValidationRule {

    boolean validate();

    void validateWithException();

}
