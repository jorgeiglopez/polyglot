package com.polyglot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.polyglot.utils.ConfigGenerator;
import com.polyglot.utils.Mapper;
import com.polyglot.utils.validator.GenericValidator;

import java.io.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.polyglot.utils.validator.Validators.*;

public class FileService {

    public static SortedMap<String, String> loadFileSourceLang(String filepath) {
        GenericValidator.of(STRING_NOT_NULL, FILE_EXIST, FILE_TYPE, FILE_EXTENSION_JSON).validate(filepath);
        return loadFile(filepath);
    }

    public static SortedMap<String, String> loadFileTargetLang(String filepath) {
        GenericValidator.of(STRING_NOT_NULL, FILE_EXTENSION_JSON).validate(filepath);
        return loadFile(filepath);
    }

    private static SortedMap<String, String> loadFile(String filepath) {
        SortedMap<String, String> translation = new TreeMap<>();

        try (InputStream inputStream = new FileInputStream(filepath)) {
            translation = Mapper.getMapper().readValue(inputStream, TreeMap.class);
        } catch (IOException e) {
            System.out.println("IOException while reading: ".concat(filepath));
        }

        return translation;
    }

    public static ConfigGenerator.Config loadConfig(String configPath) {
        try (InputStream inputStream = new FileInputStream(configPath)) {
            return Mapper.getMapper().readValue(inputStream, ConfigGenerator.Config.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("IOException while reading config: %s.", configPath);
            return null;
        }
    }

    public static void persistObject(String destination, Object object) {
        try {
            persistString(destination, Mapper.getMapper().writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static void persistMap(String destination, SortedMap<String, String> map) {
        try {
            String content = Mapper.getMapper().writeValueAsString(map)
                    .replaceAll(" : ", ": ")
                    .replaceAll(" ", "");
            persistString(destination, content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.printf("Error while persistMap() - file: %s", destination);
        }
    }

    public static void persistString(String destination, String parsedString) {
        File targetFile = new File(destination);
        targetFile.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(targetFile))) {
            writer.write(parsedString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void persistTranslations(Map<String, SortedMap<String, String>> allTranslations, Map<String, String> targetLangFiles, ConfigGenerator.Config config) {
        allTranslations.forEach((lang, content) -> {
            persistMap(targetLangFiles.get(lang), content);
            System.out.printf("Finished translating language [%s]%n", lang);

            if (config.getCopyLanguages().containsKey(lang)) {
                String to = config.getCopyLanguages().get(lang);
                String filepath = targetLangFiles.get(to);
                if (filepath == null || filepath.trim().length() == 0) {
                    System.out.printf(" - Error while copying [%s], it doesn't have a filepath associated.", to);
                } else {
                    persistMap(filepath, content);
                    System.out.printf(" - Finished copying [%s] language [%s]%n", lang, to);
                }
            }
        });
    }
}
