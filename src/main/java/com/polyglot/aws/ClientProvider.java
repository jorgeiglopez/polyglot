package com.polyglot.aws;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;

import static com.polyglot.utils.ConfigGenerator.AWS_PROFILE_NAME;

public class ClientProvider {

    private static TranslateClient translateClient;

    private ClientProvider() {
        // Set your own credential provider here. This setup is for an SSO account with profile already configured
        // If you have the normal '~/.aws/credentials' file, a simple chain credential provider will do the trick.
        AwsCredentialsProvider profileProvider = ProfileCredentialsProvider.builder()
                .profileName(AWS_PROFILE_NAME)
                .build();

        translateClient = TranslateClient.builder()
                .region(Region.AP_SOUTHEAST_1) // Not supported in Sydney
                .credentialsProvider(profileProvider)
                .build();
    }

    public static TranslateClient getTranslateClient() {
        if (translateClient == null) {
            new ClientProvider();
        }

        return translateClient;
    }
}
