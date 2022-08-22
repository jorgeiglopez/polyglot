package com.polyglot.service;

import com.polyglot.utils.ConfigGenerator;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.polyglot.service.FileService.*;
import static com.polyglot.service.TranslationService.translateKey;

public class ExecutorService {

    private static final int THREAD_LIMIT = 1;

    private static final java.util.concurrent.ExecutorService executorService = Executors.newFixedThreadPool(THREAD_LIMIT);

    public static java.util.concurrent.ExecutorService getExecutor() {
        return executorService;
    }

    public static void executeTranslations(ConfigGenerator.Config config) throws InterruptedException {
        final Map<String, String> targetLangFiles = config.getTargetLangFilePaths();
        final SortedMap<String, String> sourceMap = loadFileSourceLang(config.getSourceFilePath());
        final Map<String, SortedMap<String, String>> allTranslationsMatrix = new HashMap<>();

        targetLangFiles.forEach((targetLanguage, targetFile) -> {
            SortedMap<String, String> targetMap = loadFileTargetLang(targetFile);
            allTranslationsMatrix.put(targetLanguage, targetMap);

            // find all keys that are in source but not dest
            Set<String> missingKeys = new HashSet<>(sourceMap.keySet());
            missingKeys.removeAll(targetMap.keySet().stream()
                    .filter(k -> targetMap.get(k) != null && targetMap.get(k).trim().length() > 0)
                    .collect(Collectors.toSet()));

            // translate all the things
            System.out.printf("Translating %s keys from [%s] to [%s]%n", missingKeys.size(), config.getSourceLanguage(), targetLanguage);

            // If no missing keys, no tasks for execute.
            missingKeys.forEach((key) -> {
                ExecutorService.getExecutor().submit(() -> {
                    targetMap.put(key, translateKey(sourceMap.get(key), config.getSourceLanguage(), targetLanguage));
                });
            });
        });

        ExecutorService.getExecutor().shutdown();
        ExecutorService.getExecutor().awaitTermination(360, TimeUnit.MINUTES);

        targetLangFiles.putAll(config.getCopyLangFilePaths());
        persistTranslations(allTranslationsMatrix, targetLangFiles, config);
    }

}
