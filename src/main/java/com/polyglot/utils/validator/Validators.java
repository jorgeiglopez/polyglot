package com.polyglot.utils.validator;

import com.amazonaws.util.StringUtils;

import java.io.File;
import java.util.Objects;

public enum Validators {
    OBJECT_NOT_NULL(new ValidationRule<>(Objects::nonNull, "The input is null.")),
    STRING_NOT_NULL(new ValidationRule<String>(s -> !StringUtils.isNullOrEmpty(s), "Input string is null or empty.")),
    FILE_EXIST(new ValidationRule<String>(path -> new File(path).exists(), "The source file doesn't exist.")),
    FILE_TYPE(new ValidationRule<String>(path -> !new File(path).isDirectory(), "The path belongs to a directory. Only files are supported.")),
    FILE_EXTENSION_JSON(
            new ValidationRule<String>(path -> path.substring(path.length() - 5).equalsIgnoreCase(".json"), "The file extension is not supported. Only JSON is supported."));

    private final Validable<?> rule;

    Validators(Validable<?> rule) {
        this.rule = rule;
    }

    public Validable<?> getRule() {
        return rule;
    }
}
