package com.polyglot.utils.validator;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FileValidator extends GenericValidator<String> {

    private static final List<ValidationRule<String>> VALIDATORS = new LinkedList<>();

    private static void throwException(String message, String path) {
        final String formatted = String.format(message, path);
        throw new IllegalArgumentException(formatted);
    }

    static {
        //        VALIDATORS.add(Objects::requireNonNull);
        VALIDATORS.add(path -> {
            if (!new File(path).exists()) {
                throwException("The source file [%s] doesn't exist.", path);
            }
        });
        VALIDATORS.add(path -> {
            if (new File(path).isDirectory()) {
                throwException("The path [%s] belongs to a directory. Only files supported.", path);
            }
        });
        VALIDATORS.add(path -> {
            if (!path.substring(path.length() - 5).equalsIgnoreCase(".json")) {
                throwException("The file extension of [%s] is not supported. Only JSON is supported.", path);
            }
        });
    }

    public FileValidator() {
        super(VALIDATORS);
    }
}
