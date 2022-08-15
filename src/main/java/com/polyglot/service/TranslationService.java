package com.polyglot.service;

import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.polyglot.aws.AwsTranslateClient;
import com.polyglot.utils.validator.GenericValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.polyglot.utils.validator.Validators.STRING_NOT_NULL;

public class TranslationService {

    private static final Pattern REFERENCES = Pattern.compile("\\$t\\(.*?\\)");

    private static final Pattern VARIABLES = Pattern.compile("\\{\\{.*?\\}\\}");

    public static String translateKey(final String value, final String fromLang, final String toLang) {
        new GenericValidator<String>(STRING_NOT_NULL).validate(value, fromLang, toLang);
        String valueForTranslation = value;
        StringBuilder placeholders = new StringBuilder();
        final List<String> replacements = new ArrayList<>();

        // don't want to translate cross-references, e.g. $t(fc.om.ui.order.title)
        Matcher refMatcher = REFERENCES.matcher(valueForTranslation);
        while (refMatcher.find()) {
            String match = refMatcher.group();
            valueForTranslation = valueForTranslation.replace(match, String.format("[%s]", replacements.size()));
            placeholders.append(valueForTranslation.trim());
            replacements.add(match);
        }

        // don't want to translate variable names, e.g. {{order.ref}}
        Matcher varMatcher = VARIABLES.matcher(valueForTranslation);
        while (varMatcher.find()) {
            String match = varMatcher.group();
            valueForTranslation = valueForTranslation.replace(match, String.format("[%s]", replacements.size()));
            placeholders.append(valueForTranslation.trim());
            replacements.add(match);
        }

        String response = valueForTranslation;
        if (!placeholders.toString().trim().equalsIgnoreCase(valueForTranslation)) {
            TranslateTextRequest request = new TranslateTextRequest()
                    .withText(valueForTranslation)
                    .withSourceLanguageCode(fromLang)
                    .withTargetLanguageCode(toLang);
            try {
                response = AwsTranslateClient.getAwsTranslateClient().translateText(request).getTranslatedText();
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

        System.out.printf("   - [%s => %s]: [%s  ===>  %s]%n", fromLang, toLang, value, response);
        return response;
    }
}
