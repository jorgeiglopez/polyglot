package com.polyglot.service;

import com.polyglot.utils.ConfigGenerator;
import com.polyglot.utils.validator.GenericValidator;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.utils.ImmutableMap;

import java.io.File;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.polyglot.utils.validator.Validators.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileServiceTest {

    public static final String TEST_CONFIG_PATH = "src/test/resources/configuration/config.json";

    @Test
    public void givenEnglishFileWhenLoadingThenTheSixEntriesShouldBeRead() {
        SortedMap<String, String> readEnglish = FileService.loadFileSourceLang("src/test/resources/locales/en/translation.json");
        assertNotNull(readEnglish);
        assertEquals(6, readEnglish.size());
        assertEquals("The Order reference is: {{edges.0.node.ref}}", readEnglish.get("subValue2"));
        assertEquals("$t(fc.store.ui.order.title)", readEnglish.get("subAKey1"));
    }

    @Test
    public void givenTargetLanguageNonExistingWhenLoadingThenOnlyTheExtensionGetsValidated() {
        SortedMap<String, String> readNotExisting = FileService.loadFileTargetLang("src/test/resources/locales/de/translation.json");
        assertEquals(0, readNotExisting.size());
    }

    @Test
    public void givenEnglishAsTargetWhenLoadingThenShouldLoadTheSixEntries() {
        SortedMap<String, String> existing = FileService.loadFileTargetLang("src/test/resources/locales/en/translation.json");
        assertEquals(6, existing.size());
        assertEquals("[TestKey, subAKey1, subAKey2, subValue1, subValue2, subValue3]", existing.keySet().toString());
    }

    @Test
    public void givenAllTranslationsWhenPersistingThenTheFilesGetCreatedAndCopied() {
        Map<String, SortedMap<String, String>> allTranslations = new TreeMap<>();
        allTranslations.put("en", new TreeMap<>(ImmutableMap.of("key1", "Hello", "key2", "Translating {{key1}}")));
        allTranslations.put("es", new TreeMap<>(ImmutableMap.of("key1", "Hola", "key2", "Traduciendo {{key1}}")));
        allTranslations.put("fr", new TreeMap<>(ImmutableMap.of("key1", "Salut", "key2", "Traduction en cours {{key1}}")));

        Map<String, String> targetLangFiles = ImmutableMap.of(
                "en", "src/test/resources/locales/en/t.json",
                "en-US", "src/test/resources/locales/en-US/t.json",
                "es", "src/test/resources/locales/es/t.json",
                "es-MX", "src/test/resources/locales/es-MX/t.json",
                "fr", "src/test/resources/locales/fr/t.json");

        ConfigGenerator.writeConfig(TEST_CONFIG_PATH);
        ConfigGenerator.Config testConfig = ConfigGenerator.readConfiguration();
        assertNotNull(testConfig);

        FileService.persistTranslations(allTranslations, targetLangFiles, testConfig);

        Map<String, String> spanish = FileService.loadFileTargetLang(targetLangFiles.get("es"));
        Map<String, String> french = FileService.loadFileTargetLang(targetLangFiles.get("fr"));

        assertEquals(2, spanish.size());
        assertEquals(2, french.size());

        new File(TEST_CONFIG_PATH).deleteOnExit();
        targetLangFiles.values().forEach(path -> {
            File toCleanDirectory = new File(path).getParentFile();
            new File(path).deleteOnExit();
            toCleanDirectory.delete();
        });
    }

    private final GenericValidator<String> validator = new GenericValidator<>(STRING_NOT_NULL, FILE_EXIST, FILE_TYPE, FILE_EXTENSION_JSON);

    @Test
    public void testLoadFile() {
        SortedMap<String, String> myMap = FileService.loadFileSourceLang("src/test/resources/locales/en/translation.json");
        assertEquals("[TestKey, subAKey1, subAKey2, subValue1, subValue2, subValue3]", String.valueOf(myMap.keySet()));
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
