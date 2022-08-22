package com.polyglot;

import com.polyglot.utils.ConfigGenerator;
import org.junit.Test;

import java.io.File;

import static com.polyglot.service.FileServiceTest.TEST_CONFIG_PATH;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class ConfigGeneratorTest {

    @Test
    public void writeConfig() {
        // Test if the config file gets generated
        ConfigGenerator.writeConfig(TEST_CONFIG_PATH);

        ConfigGenerator.Config config = ConfigGenerator.readConfiguration();
        assertNotNull(config);
        assertEquals("en", config.getSourceLanguage());
        assertEquals("translation.json", config.getDefaultFileName());
        assertNotEquals(0, config.getTargetLanguages().length);
        assertFalse(config.getTargetLangFilePaths().isEmpty());

        new File(TEST_CONFIG_PATH).deleteOnExit();
    }
}
