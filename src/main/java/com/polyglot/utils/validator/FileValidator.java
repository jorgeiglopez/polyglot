package com.polyglot.utils.validator;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileValidator extends GenericValidator<String> {

    private static final List<Validable<String>> VALIDATORS = new LinkedList<>();

    static {
        VALIDATORS.add(new ValidationRule<>(path -> new File(path).exists(), "The source file doesn't exist."));
        VALIDATORS.add(new ValidationRule<>(path -> !new File(path).isDirectory(), "The path belongs to a directory. Only files supported."));
        VALIDATORS.add(new ValidationRule<>(path -> path.substring(path.length() - 5).equalsIgnoreCase(".json"), "The file extension is not supported. Only JSON is supported."));
    }

    public FileValidator() {
        super(VALIDATORS);
    }
}
