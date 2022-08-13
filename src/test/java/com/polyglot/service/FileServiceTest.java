package com.polyglot.service;

import com.polyglot.utils.validator.FileValidator;
import org.junit.jupiter.api.Test;

import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    @Test
    public void testLoadFile() {
        SortedMap myMap = FileService.loadFile("src/test/resources/locales/en/translation.json");
        assertEquals("[aTestKey, bTestKey, cTestKey, testKey]", String.valueOf(myMap.keySet()));

    }

    @Test
    public void testUnsupportedFile() {
        try {
            new FileValidator().validate("src/test/resources/locales/en/unsupported.txt");
        } catch (Exception ex) {
            assertEquals("java.lang.IllegalArgumentException: The file extension of "
                    + "[src/test/resources/locales/en/unsupported.txt] is not supported. Only JSON is supported.", ex.getMessage());
        }
    }

    @Test
    public void testUnexistingFile() {
        try {
            new FileValidator().validate("src/test/resources/locales/en/unexisting.txt");
        } catch (Exception ex) {
            assertEquals("java.lang.IllegalArgumentException: The source file [src/test/resources/locales/en/unexisting.txt] doesn't exist.",
                    ex.getMessage());
        }
    }

    @Test
    public void testDirectory() {
        try {
            new FileValidator().validate("src/test/resources/locales/en");

        } catch (Exception ex) {
            assertEquals("The path [src/test/resources/locales/en] belongs to a directory. Only files supported.", ex.getMessage());
        }
    }

}
