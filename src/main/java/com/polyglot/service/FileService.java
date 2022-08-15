package com.polyglot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.polyglot.utils.Mapper;
import com.polyglot.utils.validator.GenericValidator;

import java.io.*;
import java.util.*;

import static com.polyglot.Configuration.getTargetLangFiles;
import static com.polyglot.Configuration.isSourceLang;
import static com.polyglot.utils.validator.Validators.*;

public class FileService {

    private static void preLoadFileValidation(String filepath) {
        GenericValidator<String> validator = isSourceLang(filepath) ?
                new GenericValidator<>(STRING_NOT_NULL, FILE_EXIST, FILE_TYPE, FILE_EXTENSION_JSON) :
                new GenericValidator<>(STRING_NOT_NULL, FILE_EXTENSION_JSON);

        validator.validate(filepath);
        // TODO: check for duplicate keys
    }

    public static SortedMap<String, String> loadFile(String filepath) {
        preLoadFileValidation(filepath);
        SortedMap<String, String> translation = new TreeMap<>();

        try (InputStream inputStream = new FileInputStream(filepath)) {
            translation = Mapper.getMapper().readValue(inputStream, TreeMap.class);
        } catch (IOException e) {
            System.out.println("IOException while reading: ".concat(filepath));
        }

        return translation;
    }

    private static String formatJsonContent(SortedMap<String, String> rawContent) throws JsonProcessingException {
        // The mapper already has enabled SerializationFeature.INDENT_OUTPUT.
        return Mapper.getMapper().writeValueAsString(rawContent)
                .replaceAll(" : ", ": ")
                .replaceAll(" ", "");
    }

    public static void persistTranslations(Map<String, SortedMap<String, String>> allTranslations) {
        allTranslations.forEach((lang, content) -> {
            File targetFile = new File(getTargetLangFiles().get(lang));
            targetFile.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(targetFile))) {
                String contentJson = formatJsonContent(content);
                System.out.printf("Finished translating language [%s]%n", lang);
                writer.write(contentJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
