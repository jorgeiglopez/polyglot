package com.polyglot.utils.validator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    private static List<ValidationRule> validations;

    public Validator() {
        validations = new ArrayList<>();
    }

    private void addValidation(ValidationRule rule) {
        validations.add(rule);
    }

    public boolean validateAll() {
        return validations.stream().map(ValidationRule::validate).reduce(Boolean.TRUE, Boolean::logicalAnd);
    }

    public void validateAllWithExceptions() {
        validations.forEach(ValidationRule::validateWithException);
    }

    public <T> Validator addNotNullValidation(T t) {
        addValidation(new ValidateNotNull<T>(t));
        return this;
    }

    public Validator addFileExistValidation(String path) {
        addValidation(new ValidateFileExist(path));
        return this;
    }

    public Validator addNotDirectoryValidation(String path) {
        addValidation(new ValidateIsNotDirectory(path));
        return this;
    }

    public Validator addJsonExtensionValidation(String path) {
        addValidation(new ValidateJsonExtension(path));
        return this;
    }

    static class ValidateNotNull<T> implements ValidationRule {

        private final T value;

        public ValidateNotNull(T t) {
            this.value = t;
        }

        @Override public boolean validate() {
            return this.value != null;
        }

        @Override public void validateWithException() {
            if (!validate()) {
                throw new IllegalArgumentException("The input value is null.");
            }
        }
    }

    static class ValidateFileExist implements ValidationRule {

        private final String path;

        public ValidateFileExist(String path) {
            this.path = path;
        }

        @Override public boolean validate() {
            return new File(this.path).exists();
        }

        @Override public void validateWithException() {
            new ValidateNotNull<String>(path).validateWithException();
            if (!validate()) {
                throw new IllegalArgumentException(String.format("The source file [%s] doesn't exist.", path));
            }
        }
    }

    static class ValidateIsNotDirectory implements ValidationRule {

        private final String path;

        public ValidateIsNotDirectory(String path) {
            this.path = path;
        }

        @Override public boolean validate() {
            File file = new File(path);
            return file.exists() && !file.isDirectory();
        }

        @Override public void validateWithException() {
            new ValidateFileExist(path).validateWithException();
            if (!validate()) {
                throw new IllegalArgumentException(String.format("The path [%s] belongs to a directory. Only files supported.", path));
            }
        }
    }

    static class ValidateJsonExtension implements ValidationRule {

        private final String path;

        public ValidateJsonExtension(String path) {
            this.path = path;
        }

        @Override public boolean validate() {
            return !path.isEmpty() && path.substring(path.length() - 5).equalsIgnoreCase(".json");
        }

        @Override public void validateWithException() {
            if (!validate()) {
                throw new IllegalArgumentException(String.format("The file extension of [%s] is not supported. Only JSON is supported.", path));
            }
        }
    }

}
