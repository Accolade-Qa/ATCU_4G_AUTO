package com.aepl.atcu.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;

public class RandomGeneratorUtils {

    private static final Logger logger = LogManager.getLogger(RandomGeneratorUtils.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";

    public String generateRandomString(int length) {
        if (length <= 0) {
            logger.warn("Requested random string with non-positive length: {}", length);
            return "";
        }

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(ALPHABETS.charAt(RANDOM.nextInt(ALPHABETS.length())));
        }

        String randomString = result.toString();
        logger.debug("Generated random string of length {}", length); // avoid logging value
        return randomString;
    }

    public String generateRandomNumber(int length) {
        if (length <= 0) {
            logger.warn("Requested random number with non-positive length: {}", length);
            return "";
        }

        StringBuilder result = new StringBuilder(length);

        // First digit constraint (7, 8, or 9)
        int[] allowedFirstDigits = {7, 8, 9};
        result.append(allowedFirstDigits[RANDOM.nextInt(allowedFirstDigits.length)]);

        for (int i = 1; i < length; i++) {
            result.append(DIGITS.charAt(RANDOM.nextInt(10)));
        }

        String randomNumber = result.toString();
        logger.debug("Generated random number of length {}", length);
        return randomNumber;
    }

    public String generateRandomEmail() {
        String prefix = generateRandomString(7).toLowerCase();
        String domain = "gmail.com";
        String email = prefix + "@" + domain;

        logger.debug("Generated random email");
        return email;
    }

    public String generateRandomUIN() {
        StringBuilder uin = new StringBuilder("ACON4SA");
        for (int i = 0; i < 12; i++) {
            uin.append(RANDOM.nextInt(10));
        }

        String finalUIN = uin.toString();
        logger.debug("Generated random UIN");
        return finalUIN;
    }

    public String generateRandomIMEI() {
        // Generate first 14 digits
        StringBuilder imei = new StringBuilder(14);
        for (int i = 0; i < 14; i++) {
            imei.append(RANDOM.nextInt(10));
        }

        // Calculate Luhn check digit
        int checkDigit = calculateLuhnCheckDigit(imei.toString());
        imei.append(checkDigit);

        String finalIMEI = imei.toString();
        logger.debug("Generated random IMEI");
        return finalIMEI;
    }

    public String generateRandomICCID() {
        // ICCID typically starts with telecom identifier like 89
        StringBuilder iccid = new StringBuilder("89");

        while (iccid.length() < 19) { // 19 digits before Luhn
            iccid.append(RANDOM.nextInt(10));
        }

        int checkDigit = calculateLuhnCheckDigit(iccid.toString());
        iccid.append(checkDigit);

        String finalICCID = iccid.toString();
        logger.debug("Generated random ICCID");
        return finalICCID;
    }

    // ------------------ Helper ------------------

    private int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }
}
