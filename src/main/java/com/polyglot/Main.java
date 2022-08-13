package com.polyglot;

import com.polyglot.service.ExecutorService;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.polyglot.Configuration.*;
import static com.polyglot.service.FileService.loadFile;
import static com.polyglot.service.FileService.persistTranslations;
import static com.polyglot.service.TranslationService.translateKey;

public class Main {

    //    private static final int THREAD_LIMIT = 1;
    //
    //    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_LIMIT);

    public static void main(String[] args) throws InterruptedException {
        final String sourceLanguage = SOURCE_LANGUAGE; // English by default
        final String sourceLangFile = getDefaultLangPath();

        final Map<String, String> targetLangFiles = getTargetLangFiles(); // From Configuration

        SortedMap<String, String> sourceMap = new TreeMap<>();

        Set<String> alwaysTranslate = new HashSet<>();
        if (sourceLangFile.contains("|")) {
            String[] sources = sourceLangFile.split("\\|");
            sourceMap.putAll(loadFile(sources[1]));
            SortedMap<String, String> oldMap = loadFile(sources[0]);
            alwaysTranslate.addAll(
                    sourceMap.keySet().stream()
                            .filter(k -> !sourceMap.get(k).equals(oldMap.get(k)))
                            .collect(Collectors.toSet()));
        } else {
            sourceMap.putAll(loadFile(sourceLangFile));
        }

        final Map<String, SortedMap<String, String>> allTranslationsMatrix = new HashMap<>();

        targetLangFiles.forEach((targetLanguage, targetFile) -> {

            SortedMap<String, String> targetMap = loadFile(targetFile);
            allTranslationsMatrix.put(targetLanguage, targetMap); // load all the translations in the file to ALL Translations

            // find all keys that are in source but not dest
            Set<String> missingKeys = new HashSet<>(sourceMap.keySet());
            missingKeys.removeAll(targetMap.keySet().stream()
                    .filter(k -> !(targetMap.get(k) == null || alwaysTranslate.contains(k)))
                    .collect(Collectors.toSet()));

            // translate all the things
            System.out.println(String.format("Translating %s keys from [%s] to [%s]", missingKeys.size(), sourceLanguage, targetLanguage));

            // If no missing keys, no tasks for execute.
            missingKeys.forEach((key) -> {
                ExecutorService.getExecutor().submit(() -> {
                    targetMap.put(key, translateKey(sourceMap.get(key), sourceLanguage, targetLanguage));
                });
            });
        });

        ExecutorService.getExecutor().shutdown();
        ExecutorService.getExecutor().awaitTermination(360, TimeUnit.MINUTES);

        persistTranslations(allTranslationsMatrix);
    }

}
