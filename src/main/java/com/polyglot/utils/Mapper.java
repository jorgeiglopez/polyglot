package com.polyglot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Mapper {

    private static ObjectMapper MAPPER;

    // Todo: Handle arrays output format
    private Mapper() {
        MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static ObjectMapper getMapper() {
        if (MAPPER == null) {
            new Mapper();
        }

        return MAPPER;
    }
}
