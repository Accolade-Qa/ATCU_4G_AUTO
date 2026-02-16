package com.aepl.atcu.api.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AisCrmApiClient {

	private static final Logger logger = LogManager.getLogger(AisCrmApiClient.class);

	private final String baseUri;

	public AisCrmApiClient(String baseUri) {
		this.baseUri = baseUri;
		logger.info("Initialized AIS CRM API client with base URI: {}", baseUri);
	}

	public String generateToken(String username, String password) {
		Map<String, String> credentials = new HashMap<>();
		credentials.put("username", username);
		credentials.put("password", password);
		logger.info("Generating AIS CRM token for user: {}", username);

		Response response = RestAssured.given().baseUri(baseUri).header("Content-Type", "application/json")
				.body(credentials).post("/api/crm/generateToken");
		logger.info("AIS generateToken response status: {}", response.getStatusCode());

		return response.jsonPath().getString("token");
	}

	public Response generateTickets(String token, JSONArray payload) {
		logger.info("Calling AIS generateTickets API with payload size: {}", payload.length());
		RequestSpecification request = RestAssured.given().baseUri(baseUri).header("Content-Type", "application/json")
				.header("token", token).body(payload.toString());
		Response response = request.post("/api/crm/generateTickets");
		logger.info("AIS generateTickets response status: {}", response.getStatusCode());
		return response;
	}

	public Response getTicketStatus(String token, JSONArray payload) {
		logger.info("Calling AIS getTicketStatus API with payload size: {}", payload.length());
		RequestSpecification request = RestAssured.given().baseUri(baseUri).header("Content-Type", "application/json")
				.header("token", token).body(payload.toString());
		Response response = request.post("/api/crm/getTicketStatus");
		logger.info("AIS getTicketStatus response status: {}", response.getStatusCode());
		return response;
	}
}
