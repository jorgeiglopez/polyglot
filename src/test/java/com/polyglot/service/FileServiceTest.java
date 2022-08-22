package com.polyglot.service;

import com.polyglot.utils.validator.GenericValidator;
import org.junit.jupiter.api.Test;

import java.util.SortedMap;

import static com.polyglot.utils.validator.Validators.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileServiceTest {

    private final GenericValidator<String> validator = new GenericValidator<>(STRING_NOT_NULL, FILE_EXIST, FILE_TYPE, FILE_EXTENSION_JSON);

    @Test
    public void testLoadFile() {
        SortedMap<String, String> myMap = FileService.loadFileSourceLang("src/test/resources/locales/en/translation.json");
        assertEquals("[aTestKey, bTestKey, cTestKey, testKey]", String.valueOf(myMap.keySet()));

    }

    @Test
    public void testUnsupportedFileExtension() {
        try {
            validator.validate("src/test/resources/locales/en/unsupported.txt");
        } catch (Exception ex) {
            assertEquals("The file extension is not supported. Only JSON is supported.", ex.getMessage());
        }
    }

    @Test
    public void testUnexistingFile() {
        try {
            validator.validate("src/test/resources/locales/en/unexisting.txt");
        } catch (Exception ex) {
            assertEquals("The source file doesn't exist.",
                    ex.getMessage());
        }
    }

    @Test
    public void testDirectory() {
        try {
            validator.validate("src/test/resources/locales/en");

        } catch (Exception ex) {
            assertEquals("The path belongs to a directory. Only files are supported.", ex.getMessage());
        }
    }

}
