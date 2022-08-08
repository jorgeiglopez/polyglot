package com.polyglot;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Configuration {

    // For test
    public static final String LOCALES_FOLDER_PATH = "src/main/resources/locales";

    public static final String SOURCE_LANGUAGE = "en";

    public static final String FILE_NAME = "translation.json";

    public static final String[] TARGET_LANGUAGE = new String[] {
            //  Included to re-write the English file with the keys in alphabetical order.
            "en",    // English
            "es",    // Spanish
            "fr",    // French
    };

    public static String getDefaultLangPath() {
        return getFilePath(SOURCE_LANGUAGE);
    }

    public static String getFilePath(String lang) {
        return LOCALES_FOLDER_PATH.concat(File.separator).concat(lang).concat(File.separator).concat(FILE_NAME);
    }

    public static Map<String, String> getTargetLangFiles() {
        return Arrays.stream(TARGET_LANGUAGE).collect(Collectors.toMap(Function.identity(), Configuration::getFilePath));
    }

}
