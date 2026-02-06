package com.aepl.atcu.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Result {
    PASS("PASS"),
    FAIL("FAIL"),
    ERROR("ERROR"),
    ISSUE("ISSUE");

    private final String value;
    private static final Logger logger = LogManager.getLogger(Result.class);

    Result(String value) {
        this.value = value;
    }

    public String getValue() {
        logger.debug("Getting value: {}", value);
        return value;
    }

    public static Result fromValue(String value) {
        logger.debug("Converting string to Result enum: {}", value);
        for (Result result : Result.values()) {
            if (result.value.equalsIgnoreCase(value)) {
                logger.debug("Matched enum: {}", result);
                return result;
            }
        }
        logger.warn("No matching enum found for value: {}", value);
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
