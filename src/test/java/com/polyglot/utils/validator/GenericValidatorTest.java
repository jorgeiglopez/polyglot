package com.polyglot.utils.validator;

import org.junit.Test;

import static com.polyglot.utils.validator.Validators.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericValidatorTest {

    @Test
    public <T> void givenANullObjectWhenValidatedThenTrowException() {
        try {
            GenericValidator.of(OBJECT_NOT_NULL).validate((T) null);
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals("The input is null.", e.getMessage());
        }
    }

    @Test
    public void givenEmptyStringWhenValidatedThenTrowException() {
        try {
            GenericValidator.of(STRING_NOT_NULL).validate(" ");
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals("Input string is null or empty.", e.getMessage());
        }
    }

    @Test
    public void givenNotExistingFileWhenValidatedThenTrowException() {
        try {
            GenericValidator.of(FILE_EXIST).validate("notExisting.json");
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals("The source file doesn't exist.", e.getMessage());
        }
    }

    @Test
    public void givenADirectoryWhenValidatedThenTrowException() {
        try {
            GenericValidator.of(FILE_TYPE).validate("src/test/resources/locales/en");
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals("The path belongs to a directory. Only files are supported.", e.getMessage());
        }
    }

    @Test
    public void givenNotSupportedExtensionWhenValidatedThenTrowException() {
        try {
            GenericValidator.of(FILE_EXTENSION_JSON).validate("WrongExtension.txt");
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals("The file extension is not supported. Only JSON is supported.", e.getMessage());
        }
    }
}
