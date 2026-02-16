package com.aepl.atcu.api.client;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AisCrmApiClient {

	private final String baseUri;

	public AisCrmApiClient(String baseUri) {
		this.baseUri = baseUri;
	}

	public String generateToken(String username, String password) {
		Map<String, String> credentials = new HashMap<>();
		credentials.put("username", username);
		credentials.put("password", password);

		Response response = RestAssured.given().baseUri(baseUri).header("Content-Type", "application/json")
				.body(credentials).post("/api/crm/generateToken");

		return response.jsonPath().getString("token");
	}

	public Response generateTickets(String token, JSONArray payload) {
		RequestSpecification request = RestAssured.given().baseUri(baseUri).header("Content-Type", "application/json")
				.header("token", token).body(payload.toString());
		return request.post("/api/crm/generateTickets");
	}

	public Response getTicketStatus(String token, JSONArray payload) {
		RequestSpecification request = RestAssured.given().baseUri(baseUri).header("Content-Type", "application/json")
				.header("token", token).body(payload.toString());
		return request.post("/api/crm/getTicketStatus");
	}
}
