package com.aepl.atcu.util;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.mail.BodyPart;
import jakarta.mail.Flags;
import jakarta.mail.Flags.Flag;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.FlagTerm;

public class EmailReader {
	private static final Logger logger = LogManager.getLogger(EmailReader.class);

	public static String getPasswordFromOutlook() throws Exception {
		String host = "imap-mail.outlook.com";
		String username = ConfigProperties.getProperty("username");
		String password = ConfigProperties.getProperty("password");

		logger.info("Connecting to Outlook with username={}, password length={}", username,
				password == null ? 0 : password.length());

		Properties props = new Properties();
		props.put("mail.imap.host", host);
		props.put("mail.imap.port", "993");
		props.put("mail.imap.ssl.enable", "true");

		Session session = Session.getInstance(props);
		try (Store store = session.getStore("imap")) {
			store.connect(host, username, password);

			try (Folder inbox = store.getFolder("INBOX")) {
				inbox.open(Folder.READ_WRITE);

				// Wait up to 60 seconds for the email
				for (int attempt = 0; attempt < 6; attempt++) {
					Message[] messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
					if (messages.length > 0) {
						for (int i = messages.length - 1; i >= 0; i--) {
							Message msg = messages[i];
							String content = getTextFromMessage(msg);

							if (content.toLowerCase().contains("password") || content.toLowerCase().contains("otp")) {
								Pattern pattern = Pattern.compile(
										"(?<=password[:\\s]*)([A-Za-z0-9!@#$%^&*()_+=-]{6,20})",
										Pattern.CASE_INSENSITIVE);
								Matcher matcher = pattern.matcher(content);

								if (matcher.find()) {
									msg.setFlag(Flag.SEEN, true);
									return matcher.group(1);
								}
							}
						}
					}
					Thread.sleep(10000); // wait 10s between checks
				}
			}
		}
		throw new Exception("No password or OTP found in unread emails after retries.");
	}

	private static String getTextFromMessage(Message message) throws Exception {
		Object content = message.getContent();
		if (content instanceof String) {
			return (String) content;
		} else if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart part = multipart.getBodyPart(i);
				if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
					result.append(part.getContent());
				}
			}
			return result.toString();
		}
		return "";
	}
}
