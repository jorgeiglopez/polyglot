package com.polyglot.service;

import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import com.amazonaws.util.StringUtils;
import com.polyglot.aws.AwsTranslateClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationService {

    private static final Pattern REFERENCES = Pattern.compile("\\$t\\(.*?\\)");

    private static final Pattern VARIABLES = Pattern.compile("\\{\\{.*?\\}\\}");

    public static String translateKey(final String value, final String fromLang, final String toLang) {
        final List<String> replacements = new ArrayList<>();
        String valueForTranslation = value;

        // don't want to translate cross-references, e.g. $t(fc.om.ui.order.title)
        Matcher refMatcher = REFERENCES.matcher(valueForTranslation);
        while (refMatcher.find()) {
            String match = refMatcher.group();
            valueForTranslation = valueForTranslation.replace(match, String.format("[%s]", replacements.size()));
            replacements.add(match);
        }

        // don't want to translate variable names, e.g. {{order.ref}}
        Matcher varMatcher = VARIABLES.matcher(valueForTranslation);
        while (varMatcher.find()) {
            String match = varMatcher.group();
            valueForTranslation = valueForTranslation.replace(match, String.format("[%s]", replacements.size()));
            replacements.add(match);
        }

        if (StringUtils.isNullOrEmpty(valueForTranslation)) {
            return value;
        }

        TranslateTextRequest request = new TranslateTextRequest()
                .withText(valueForTranslation)
                .withSourceLanguageCode(fromLang)
                .withTargetLanguageCode(toLang);

        try {
            TranslateTextResult result = AwsTranslateClient.getAwsTranslateClient().translateText(request);
            String response = result.getTranslatedText();

            // swap our references and variables back in
            for (int i = 0; i < replacements.size(); i++) {
                response = response.replaceAll(
                        String.format("\\[[\\s]*%s[\\s]*\\]", i),
                        Matcher.quoteReplacement(replacements.get(i))
                );
            }

            System.out.println(String.format(" - [%s => %s]: [%s  ===>  %s]", fromLang, toLang, value, response));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
