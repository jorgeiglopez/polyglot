package com.polyglot.service;

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
            FileService.validateFile("src/test/resources/locales/en/unsupported.txt");
        } catch (Exception ex) {
            assertEquals("The file extension of [src/test/resources/locales/en/unsupported.txt] is not supported. Only JSON is supported.", ex.getMessage());
        }
    }

    @Test
    public void testUnexistingFile() {
        try {
            FileService.validateFile("src/test/resources/locales/en/unexisting.txt");
        } catch (Exception ex) {
            assertEquals("The source file [src/test/resources/locales/en/unexisting.txt] doesn't exist.", ex.getMessage());
        }
    }

    @Test
    public void testDirectory() {
        try {
            FileService.validateFile("src/test/resources/locales/en");
        } catch (Exception ex) {
            assertEquals("The source file [src/test/resources/locales/en] can only be a regular file. Directories are not allowed.", ex.getMessage());
        }
    }

    @Test
    public void testUnreadableFile() {
        try {
            FileService.validateFile("src/test/resources/locales/en/unreadable.json");
        } catch (Exception ex) {
            assertEquals("The source file [src/test/resources/locales/en/unreadable.json] can't be read. Check permissions.", ex.getMessage());
        }
    }

}
