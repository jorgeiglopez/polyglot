package com.polyglot.utils.validator;

public interface Validable<T> {

    void validate(T t);

    void validate(T... t);

}
