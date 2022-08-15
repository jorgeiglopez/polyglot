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
    public void testUnsupportedFileExtension() {
        try {
            new FileValidator().validate("src/test/resources/locales/en/unsupported.txt");
        } catch (Exception ex) {
            assertEquals("The file extension is not supported. Only JSON is supported.", ex.getMessage());
        }
    }

    @Test
    public void testUnexistingFile() {
        try {
            new FileValidator().validate("src/test/resources/locales/en/unexisting.txt");
        } catch (Exception ex) {
            assertEquals("The source file doesn't exist.",
                    ex.getMessage());
        }
    }

    @Test
    public void testDirectory() {
        try {
            new FileValidator().validate("src/test/resources/locales/en");

        } catch (Exception ex) {
            assertEquals("The path belongs to a directory. Only files supported.", ex.getMessage());
        }
    }

}
