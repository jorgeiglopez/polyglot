package com.polyglot.aws;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;

public class AwsTranslateClient {

    private static AmazonTranslate translateClient;

    private AwsTranslateClient() {
        // Not supported in Sydney
        final String REGION = "ap-southeast-1";

        translateClient = AmazonTranslateClient.builder()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(REGION)
                .build();
    }

    public static AmazonTranslate getAwsTranslateClient() {
        if (translateClient == null) {
            new AwsTranslateClient();
        }

        return translateClient;
    }
}
