package com.aepl.atcu.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import com.aepl.atcu.api.client.AisCrmApiClient;
import com.aepl.atcu.api.model.AisTicketContext;
import com.aepl.atcu.api.model.AisTicketRequestBuilder;
import com.aepl.atcu.util.DotEnvUtil;
import com.aepl.atcu.util.RandomGeneratorUtils;

import io.restassured.response.Response;

public class AisTicketService {

	private static final Logger logger = LogManager.getLogger(AisTicketService.class);

	private static final String AIS_CRM_BASE_URI_KEY = "AIS_CRM_BASE_URI";
	private static final String AIS_CRM_USERNAME_KEY = "AIS_CRM_USERNAME";
	private static final String AIS_CRM_PASSWORD_KEY = "AIS_CRM_PASSWORD";

	private final AisCrmApiClient apiClient;
	private final RandomGeneratorUtils randomUtils;
	private final String username;
	private final String password;

	public AisTicketService() {
		String baseUri = requireEnv(AIS_CRM_BASE_URI_KEY);
		this.username = requireEnv(AIS_CRM_USERNAME_KEY);
		this.password = requireEnv(AIS_CRM_PASSWORD_KEY);
		this.apiClient = new AisCrmApiClient(baseUri);
		this.randomUtils = new RandomGeneratorUtils();
	}

	public String generateToken() {
		return apiClient.generateToken(username, password);
	}

	public AisTicketContext generateAndFetchTicket() {
		AisTicketContext context = new AisTicketContext();
		context.setVinNo(randomUtils.generateRandomString(17));
		context.setIccid("89916490634628942181");
		context.setUinNo(randomUtils.generateRandomString(19));
		context.setDeviceImei(randomUtils.generateRandomNumber(15));

		String token = generateToken();
		if (token == null || token.isBlank()) {
			throw new RuntimeException("Failed to generate AIS CRM token.");
		}

		JSONArray generatePayload = AisTicketRequestBuilder.buildGenerateTicketPayload(context, randomUtils);
		Response generateResponse = apiClient.generateTickets(token, generatePayload);
		if (generateResponse.getStatusCode() >= 400) {
			throw new RuntimeException("AIS generateTickets API failed with status: " + generateResponse.getStatusCode());
		}

		String ticketNo = fetchTicketStatus(context.getVinNo(), context.getIccid(), token);
		context.setTicketNo(ticketNo);
		logger.info("Generated AIS ticket: {}", ticketNo);
		return context;
	}

	public String fetchTicketStatus(String vinNo, String iccid, String token) {
		JSONArray statusPayload = AisTicketRequestBuilder.buildTicketStatusPayload(vinNo, iccid);
		Response statusResponse = apiClient.getTicketStatus(token, statusPayload);
		if (statusResponse.getStatusCode() >= 400) {
			throw new RuntimeException("AIS getTicketStatus API failed with status: " + statusResponse.getStatusCode());
		}
		String ticketNo = statusResponse.jsonPath().getString("data[0].Ticket_No");
		if (ticketNo == null || ticketNo.isBlank()) {
			throw new RuntimeException("Ticket_No not found in AIS getTicketStatus response.");
		}
		return ticketNo;
	}

	private String requireEnv(String key) {
		String value = DotEnvUtil.get(key);
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("Missing required environment key: " + key + " (.env)");
		}
		return value;
	}
}
