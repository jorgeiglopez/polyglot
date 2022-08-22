package com.polyglot.service;

import com.polyglot.aws.ClientProvider;
import com.polyglot.utils.validator.GenericValidator;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.polyglot.utils.validator.Validators.STRING_NOT_NULL;

public class TranslationService {

    private static final Pattern REFERENCES = Pattern.compile("\\$t\\(.*?\\)");

    private static final Pattern VARIABLES = Pattern.compile("\\{\\{.*?\\}\\}");

    public static String translateKey(final String value, final String fromLang, final String toLang) {
        GenericValidator.of(STRING_NOT_NULL).validate(value, fromLang, toLang);
        String valueForTranslation = value;
        StringBuilder placeholders = new StringBuilder();
        final List<String> replacements = new ArrayList<>();

        // don't want to translate cross-references, e.g. $t(order.title)
        Matcher refMatcher = REFERENCES.matcher(valueForTranslation);
        while (refMatcher.find()) {
            String match = refMatcher.group();
            final String placeholder = String.format("[%s]", replacements.size());
            valueForTranslation = valueForTranslation.replace(match, placeholder);
            placeholders.append(placeholder);
            replacements.add(match);
        }

        // don't want to translate variable names, e.g. {{node.date}}
        Matcher varMatcher = VARIABLES.matcher(valueForTranslation);
        while (varMatcher.find()) {
            String match = varMatcher.group();
            final String placeholder = String.format("[%s]", replacements.size());
            valueForTranslation = valueForTranslation.replace(match, placeholder);
            placeholders.append(placeholder);
            replacements.add(match);
        }

        String response = valueForTranslation;
        if (!placeholders.toString().trim().equalsIgnoreCase(valueForTranslation)) {
            // TODO: Implement batch translations

            TranslateTextRequest request = TranslateTextRequest.builder()
                    .text(valueForTranslation)
                    .sourceLanguageCode(fromLang)
                    .targetLanguageCode(toLang)
                    .build();
            try {
                response = ClientProvider.getTranslateClient().translateText(request).translatedText();
                if (response.equals(valueForTranslation)) { // Avoid writing a value without translation.
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        if (replacements.size() > 0) {
            for (int i = 0; i < replacements.size(); i++) {
                response = response.replaceAll(
                        String.format("\\[[\\s]*%s[\\s]*\\]", i),
                        Matcher.quoteReplacement(replacements.get(i))
                );
            }
        }

        System.out.printf(" - [%s => %s]: [%s  ===>  %s]%n", fromLang, toLang, value, response);
        return response;
    }
}
