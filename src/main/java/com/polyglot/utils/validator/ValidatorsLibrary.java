package com.polyglot.utils.validator;

import java.io.File;
import java.util.Objects;

public enum ValidatorsLibrary {
    OBJECT_NOT_NULL(new ValidationRule<>(Objects::nonNull, "The input is null.")),
    FILE_EXIST(new ValidationRule<String>(path -> new File(path).exists(), "The source file doesn't exist.")),
    FILE_TYPE(new ValidationRule<String>(path -> !new File(path).isDirectory(), "The path belongs to a directory. Only files supported.")),
    FILE_EXTENSION(new ValidationRule<String>(path -> path.substring(path.length() - 5).equalsIgnoreCase(".json"), "The file extension is not supported. Only JSON is supported."));

    private final Validable<?> rule;

    ValidatorsLibrary(Validable<?> rule) {
        this.rule = rule;
    }

    public Validable<?> getRule() {
        return rule;
    }
}
