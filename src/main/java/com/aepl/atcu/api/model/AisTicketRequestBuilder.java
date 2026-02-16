package com.aepl.atcu.api.model;

import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aepl.atcu.util.RandomGeneratorUtils;

public final class AisTicketRequestBuilder {

	private AisTicketRequestBuilder() {
	}

	public static JSONArray buildGenerateTicketPayload(AisTicketContext context, RandomGeneratorUtils random) {
		JSONObject object = new JSONObject();
		object.put("VIN_NO", context.getVinNo());
		object.put("ICCID", context.getIccid());
		object.put("UIN_NO", context.getUinNo());
		object.put("DEVICE_IMEI", context.getDeviceImei());
		object.put("DEVICE_MAKE", "Accolade");
		object.put("DEVICE_MODEL", "AEPL051400");
		object.put("ENGINE_NO", random.generateRandomString(20));
		object.put("REG_NUMBER", random.generateRandomString(12));
		object.put("VEHICLE_OWNER_FIRST_NAME", random.generateRandomString(8));
		object.put("VEHICLE_OWNER_MIDDLE_NAME", random.generateRandomString(8));
		object.put("VEHICLE_OWNER_LAST_NAME", random.generateRandomString(8));
		object.put("ADDRESS_LINE_1", random.generateRandomString(20));
		object.put("ADDRESS_LINE_2", random.generateRandomString(20));
		object.put("VEHICLE_OWNER_CITY", random.generateRandomString(10));
		object.put("VEHICLE_OWNER_DISTRICT", random.generateRandomString(10));
		object.put("VEHICLE_OWNER_STATE", random.generateRandomString(10));
		object.put("VEHICLE_OWNER_COUNTRY", "India");
		object.put("VEHICLE_OWNER_PINCODE", random.generateRandomNumber(6));
		object.put("VEHICLE_OWNER_REGISTERED_MOBILE", random.generateRandomNumber(10));
		object.put("DEALER_CODE", random.generateRandomNumber(6));
		object.put("POS_CODE", random.generateRandomString(8));
		object.put("POA_DOC_NAME", "AADHAR");
		object.put("POA_DOC_NO", random.generateRandomNumber(12));
		object.put("POI_DOC_TYPE", "PAN");
		object.put("POI_DOC_NO", random.generateRandomString(10));
		object.put("RTO_OFFICE_CODE", random.generateRandomString(6));
		object.put("RTO_STATE", random.generateRandomString(8));
		object.put("PRIMARY_OPERATOR", "BSNL");
		object.put("SECONDARY_OPERATOR", "BHA");
		object.put("PRIMARY_MOBILE_NUMBER", random.generateRandomNumber(10));
		object.put("SECONDARY_MOBILE_NUMBER", random.generateRandomNumber(10));
		object.put("VEHICLE_MODEL", random.generateRandomString(10));
		object.put("COMMERCIAL_ACTIVATION_START_DATE", LocalDate.now().minusDays(1));
		object.put("COMMERCIAL_ACTIVATION_EXPIRY_DATE", LocalDate.now().plusYears(2));
		object.put("MFG_YEAR", 2025);
		object.put("INVOICE_DATE", LocalDate.now().toString());
		object.put("INVOICE_NUMBER", random.generateRandomString(12));
		object.put("CERTIFICATE_VALIDITY_DURATION_IN_YEAR", 2);

		JSONArray payload = new JSONArray();
		payload.put(object);
		return payload;
	}

	public static JSONArray buildTicketStatusPayload(String vinNo, String iccid) {
		JSONObject object = new JSONObject();
		object.put("VIN_NO", vinNo);
		object.put("ICCID", iccid);
		JSONArray payload = new JSONArray();
		payload.put(object);
		return payload;
	}
}
