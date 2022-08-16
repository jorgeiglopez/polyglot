package com.polyglot;

import com.polyglot.service.FileService;
import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigGenerator {

    private static final String localesFolderTest = "src/main/resources/locales"; // For testing

    private static final String sourceLanguage = "en";

    private static final String defaultFileName = "translation.json";

    private static final String[] findReplace = new String[] {
            "en",   // English //  Included to re-write the English file with the keys in alphabetical order.
            "es",   // Spanish
            "fr",   // French
            //            "af",	// Afrikaans
            //            "sq",	// Albanian
            //            "am",	// Amharic
            //            "ar",	// Arabic
            //            "az",	// Azerbaijani
            //            "bn",	// Bengali
            //            "bs",	// Bosnian
            //            "bg",	// Bulgarian
            //            "hr",	// Croatian
            //            "cs",	// Czech
            //            "da",	// Danish
            //            "nl",	// Dutch
            //            "en",	// English
            //            "et",	// Estonian
            //            "fi",	// Finnish
            //            "fr",	// French
            //            "ka",	// Georgian
            //            "de",	// German
            //            "el",	// Greek
            //            "ha",	// Hausa
            //            "he",	// Hebrew
            //            "hi",	// Hindi
            //            "hu",	// Hungarian
            //            "id",	// Indonesian
            //            "it",	// Italian
            //            "ja",	// Japanese
            //            "ko",	// Korean
            //            "lv",	// Latvian
            //            "ms",	// Malay
            //            "no",	// Norwegian
            //            "fa",	// Persian
            //            "ps",	// Pashto
            //            "pl",	// Polish
            //            "pt",	// Portuguese
            //            "ro",	// Romanian
            //            "ru",	// Russian
            //            "sr",	// Serbian
            //            "sk",	// Slovak
            //            "sl",	// Slovenian
            //            "so",	// Somali
            //            "es",	// Spanish
            //            "sw",	// Swahili
            //            "sv",	// Swedish
            //            "tl",	// Tagalog
            //            "ta",	// Tamil
            //            "th",	// Thai
            //            "tr",	// Turkish
            //            "uk",	// Ukrainian
            //            "ur",	// Urdu
            //            "vi",	// Vietnamese
            //            "zh",	// Chinese (Simplified)
            //            "fa-AF",    // Dari
            //            "fr-CA",	// French (Canadian)
            //            "zh-TW",	// Chinese (Traditional)
    };

    // TODO: Handle the copy of: "en ----> en-US"
    private static final FindReplace[] FIND_REPLACE = {
            new FindReplace("en-US", "en"),
            new FindReplace("es-MX", "es")
    };

    @Getter
    static class FindReplace {

        private final String find;

        private final String replace;

        public FindReplace(String find, String replace) {
            this.find = find;
            this.replace = replace;
        }

    }

    public static void writeConfig() {
        SortedMap<String, Object> configMap = new TreeMap<>();
        configMap.put("localesFolderTest", localesFolderTest);
        configMap.put("sourceLanguage", sourceLanguage);
        configMap.put("defaultFileName", defaultFileName);
        configMap.put("targetLanguages", findReplace);
        configMap.put("findReplace", FIND_REPLACE);

        FileService.saveFile("src/main/resources/config.json", configMap);
    }

    public static String getSourceLanguage() {
        return sourceLanguage;
    }

    public static String getDefaultLangPath() {
        return getFilePath(sourceLanguage);
    }

    public static String getFilePath(String lang) {
        return localesFolderTest.concat(File.separator).concat(lang).concat(File.separator).concat(defaultFileName);
    }

    public static Map<String, String> getTargetLangFiles() {
        return Arrays.stream(findReplace).collect(Collectors.toMap(Function.identity(), ConfigGenerator::getFilePath));
    }

    public static boolean isSourceLang(String filepath) {
        return filepath.endsWith(sourceLanguage.concat(File.separator).concat(defaultFileName));
    }
}
