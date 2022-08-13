package com.polyglot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.polyglot.utils.Mapper;
import com.polyglot.utils.validator.FileValidator;

import java.io.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.polyglot.Configuration.getTargetLangFiles;

public class FileService {

    public static SortedMap<String, String> loadFile(String filepath) {
        new FileValidator().validate(filepath);

        // TODO: check for duplicate keys
        SortedMap<String, String> translation = new TreeMap<>();

        try (InputStream inputStream = new FileInputStream(filepath)) {
            translation = Mapper.getMapper().readValue(inputStream, TreeMap.class);
        } catch (IOException e) {
            System.out.println("Warning: File " + filepath + " is missing or empty, will continue processing as if that were intended.");
        }

        return translation;
    }

    private static String formatJsonContent(SortedMap<String, String> rawContent) throws JsonProcessingException {
        // The mapper already has enabled SerializationFeature.INDENT_OUTPUT.
        return Mapper.getMapper().writeValueAsString(rawContent).replaceAll(" : ", ": ");
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
