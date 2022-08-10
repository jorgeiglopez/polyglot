package com.polyglot.service;

import com.polyglot.utils.Mapper;

import java.io.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.polyglot.Configuration.getTargetLangFiles;

public class FileService {

    protected static void validateFile(String filepath) {
        final File file = new File(filepath);
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("The source file [%s] doesn't exist.", filepath));
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException(String.format("The source file [%s] can only be a regular file. Directories are not allowed.", filepath));
        }
        if (!filepath.substring(filepath.length() - 5).equalsIgnoreCase(".json")) {
            throw new IllegalArgumentException(String.format("The file extension of [%s] is not supported. Only JSON is supported.", filepath));
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException(String.format("The source file [%s] can't be read. Check permissions.", filepath));
        }

    }

    public static SortedMap<String, String> loadFile(String filepath) {
        validateFile(filepath);
        // TODO: check for duplicate keys
        SortedMap<String, String> translation = new TreeMap<>();

        try (InputStream inputStream = new FileInputStream(filepath)) {
            translation = Mapper.getMapper().readValue(inputStream, TreeMap.class);
        } catch (IOException e) {
            System.out.println("Warning: File " + filepath + " is missing or empty, will continue processing as if that were intended.");
        }

        return translation;
    }

    public static void persistTranslations(Map<String, SortedMap<String, String>> allTranslations) {
        allTranslations.forEach((lang, content) -> {
            File targetFile = new File(getTargetLangFiles().get(lang));
            targetFile.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(targetFile))) {
                String contentJson = Mapper.getMapper().writeValueAsString(content).replaceAll(" : ", ": ");
                System.out.println(String.format("Finished translating language [%s]", lang));
                writer.write(contentJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
