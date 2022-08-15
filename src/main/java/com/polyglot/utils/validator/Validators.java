package com.polyglot.utils.validator;

import java.io.File;
import java.util.Objects;

public class Validators {

    public static ValidationRule OBJECT_NOT_NULL() {
        return new ValidationRule(Objects::nonNull, "Input can not be null");
    }

    public static ValidationRule<String> STRING_NOT_NULL() {
        return new ValidationRule<>(s -> s != null && !s.isEmpty(), "Input string is null or empty");
    }

    public static ValidationRule<String> FILE_EXIST() {
        return new ValidationRule<>(path -> new File(path).exists(), "The source file doesn't exist.");
    }

    public static ValidationRule<String> FILE_TYPE() {
        return new ValidationRule<>(path -> !new File(path).isDirectory(),
                "The path belongs to a directory. Only files supported.");
    }

    public static ValidationRule<String> FILE_EXTENSION() {
        return new ValidationRule<>(path -> path.substring(path.length() - 5).equalsIgnoreCase(".json"),
                "The file extension is not supported. Only JSON is supported.");
    }

}
