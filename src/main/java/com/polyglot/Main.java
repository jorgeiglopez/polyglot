package com.polyglot;

import com.polyglot.service.ExecutorService;

import static com.polyglot.utils.ConfigGenerator.Config;
import static com.polyglot.utils.ConfigGenerator.readConfiguration;

public class Main {

    /**
     * >> For configuration, go to: "ConfigGenerator.java"
     * ----------------
     * REMEMBER TO SIGN IN WITH YOU AWS ACCOUNT BEFORE RUNNING THE TRANSLATOR
     * EXECUTE: `aws sso login --profile="<PROFILE_NAME>"`
     * ----------------
     */
    public static void main(String[] args) {
        final Config config = readConfiguration();
        try {
            ExecutorService.executeTranslations(config);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.printf("The execution has been interrupted. %n%s", e);
        }
    }
}
