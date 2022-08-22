package com.polyglot.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polyglot.service.FileService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigGenerator {

    // Before starting, please run "aws sso login --profile=<AWS_PROFILE_NAME>" (The profile names should match)
    public static final String AWS_PROFILE_NAME = "";

    // File where the configuration will be stored
    private static final String CONFIG_PATH = "src/main/resources/config.json";

    // Replace for the project's "locales folder" location
    private static final String localesFolderPath = "src/test/resources/locales"; // For testing, replace!

    private static final String sourceLanguage = "en";

    private static final String defaultFileName = "translation.json";

    // Please do not delete or alter the order of the target languages. Use the commenting feature to select which languages will be included
    private static final String[] targetLanguages = new String[] {
            "en",   // English //  Included to re-write the English file with the keys in alphabetical order.
            "af",    // Afrikaans
            //            "sq",    // Albanian
            //            "am",    // Amharic
            //            "ar",    // Arabic
            //            "az",    // Azerbaijani
            //            "bn",    // Bengali
            //            "bs",    // Bosnian
            //            "bg",    // Bulgarian
            //            "hr",    // Croatian
            //            "cs",    // Czech
            //            "da",    // Danish
            //            "nl",    // Dutch
            //            "et",    // Estonian
            //            "fi",    // Finnish
            //            "fr",   // French
            //            "ka",    // Georgian
            //            "de",    // German
            //            "el",    // Greek
            //            "ha",    // Hausa
            //            "he",    // Hebrew
            //            "hi",    // Hindi
            //            "hu",    // Hungarian
            //            "id",    // Indonesian
            //            "it",    // Italian
            //            "ja",    // Japanese
            //            "ko",    // Korean
            //            "lv",    // Latvian
            //            "ms",    // Malay
            //            "no",    // Norwegian
            //            "fa",    // Persian
            //            "ps",    // Pashto
            //            "pl",    // Polish
            //            "pt",    // Portuguese
            //            "ro",    // Romanian
            //            "ru",    // Russian
            //            "sr",    // Serbian
            //            "sk",    // Slovak
            //            "sl",    // Slovenian
            //            "so",    // Somali
            //            "es",    // Spanish
            //            "sw",    // Swahili
            //            "sv",    // Swedish
            //            "tl",    // Tagalog
            //            "ta",    // Tamil
            //            "th",    // Thai
            //            "tr",    // Turkish
            //            "uk",    // Ukrainian
            //            "ur",    // Urdu
            //            "vi",    // Vietnamese
            //            "zh",    // Chinese (Simplified)
            //            "fa-AF",    // Dari
            //            "fr-CA",    // French (Canadian)
            //            "zh-TW",    // Chinese (Traditional)
    };

    // Translations [en -> en-US] and [en -> es-MX] are not supported, hence this files will get copied rather than translated
    private static final CopyLanguage[] copyLanguages = {
            new CopyLanguage("en", "en-US"),
            new CopyLanguage("es", "es-MX")
    };

    private static final boolean ALWAYS_OVERRIDE_CONFIG = true;

    // ========================================================================================
    // =====================  E N D   O F   C O N F I G U R A T I O N  ========================
    // ==========================  (Don't modify below this point)  ===========================
    // ========================================================================================

    public static void writeConfig() {
        writeConfig(CONFIG_PATH);
    }

    // Select which project will get translated by changing the "locales folder" argument (Core, Store, OMX and Returns)
    public static void writeConfig(String configPath) {
        // Change the first argument to target the "locales folder" from above.
        Config config = new Config(localesFolderPath, sourceLanguage, defaultFileName, targetLanguages, copyLanguages);
        FileService.persistObject(configPath, config);
    }

    public static Config readConfiguration() {
        if (ALWAYS_OVERRIDE_CONFIG || !new File(CONFIG_PATH).exists()) {
            new File(CONFIG_PATH).deleteOnExit();
            ConfigGenerator.writeConfig();
        }
        return FileService.loadConfig(CONFIG_PATH);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class CopyLanguage {

        private String from;

        private String to;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {

        private String localesFolder; // TODO: Allow locales folder to be an array

        private String sourceLanguage;

        private String defaultFileName;

        private String[] targetLanguages;

        private CopyLanguage[] copyLanguage;

        private String getFilePath(String lang) {
            return localesFolder.concat(File.separator).concat(lang).concat(File.separator).concat(defaultFileName);
        }

        @JsonIgnore
        public String getSourceFilePath() {
            return getFilePath(sourceLanguage);
        }

        public Map<String, String> getTargetLangFilePaths() {
            return Arrays.stream(targetLanguages)
                    .collect(Collectors.toMap(Function.identity(), this::getFilePath));
        }

        public Map<String, String> getCopyLangFilePaths() {
            return Arrays.stream(copyLanguage)
                    .collect(Collectors.toMap(CopyLanguage::getTo, cp -> getFilePath(cp.getTo())));
        }

        public Map<String, String> getCopyLanguages() {
            return Arrays.stream(copyLanguage)
                    .collect(Collectors.toMap(CopyLanguage::getFrom, CopyLanguage::getTo));
        }
    }
}
