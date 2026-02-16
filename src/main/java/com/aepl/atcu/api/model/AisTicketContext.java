package com.aepl.atcu.api.model;

public class AisTicketContext {

	private String vinNo;
	private String iccid;
	private String uinNo;
	private String deviceImei;
	private String ticketNo;

	public String getVinNo() {
		return vinNo;
	}

	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public String getUinNo() {
		return uinNo;
	}

	public void setUinNo(String uinNo) {
		this.uinNo = uinNo;
	}

	public String getDeviceImei() {
		return deviceImei;
	}

	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
}
