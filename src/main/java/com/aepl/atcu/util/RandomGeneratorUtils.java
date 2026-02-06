package com.aepl.atcu.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RandomGeneratorUtils {
	private static final Logger logger = LogManager.getLogger(RandomGeneratorUtils.class);

	public String generateRandomString(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder result = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int index = (int) (Math.random() * characters.length());
			result.append(characters.charAt(index));
		}

		String randomString = result.toString();
		logger.info("Generated random string of length {}: {}", length, randomString);

		return randomString;
	}

	public String generateRandomNumber(int length) {
		if (length <= 0) {
			logger.warn("Requested random number with non-positive length: {}", length);
			return "";
		}

		StringBuilder result = new StringBuilder(length);
		int[] allowedFirstDigits = { 7, 8, 9 };
		int firstDigit = allowedFirstDigits[(int) (Math.random() * allowedFirstDigits.length)];
		result.append(firstDigit);

		for (int i = 1; i < length; i++) {
			int digit = (int) (Math.random() * 10);
			result.append(digit);
		}

		String randomNumber = result.toString();
		logger.info("Generated random number of length {}: {}", length, randomNumber);

		return randomNumber;
	}

	public String generateRandomEmail() {
		String prefix = generateRandomString(7);
		String domain = "gmail.com";
		String email = prefix + "@" + domain;

		logger.info("Generated random email: {}", email);
		return email;
	}

	public String generateRandomUIN() {
		StringBuilder uin = new StringBuilder("ACON4SA");
		for (int i = 0; i < 12; i++) {
			int digit = (int) (Math.random() * 10);
			uin.append(digit);
		}
		String finalUIN = uin.toString();
		logger.info("Generated random UIN: {}", finalUIN);
		return finalUIN;
	}

	public String generateRandomIMEI() {
		StringBuilder imei = new StringBuilder();
		for (int i = 0; i < 15; i++) {
			int digit = (int) (Math.random() * 10);
			imei.append(digit);
		}
		String finalIMEI = imei.toString();
		logger.info("Generated random IMEI: {}", finalIMEI);
		return finalIMEI;
	}

	public String generateRandomICCID() {
		StringBuilder iccid = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			int digit = (int) (Math.random() * 10);
			iccid.append(digit);
		}
		String finalICCID = iccid.toString();
		logger.info("Generated random ICCID: {}", finalICCID);
		return finalICCID;
	}
}
