package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class MyAis140TicketsPageLocators {

	protected final By navBarLink = By.xpath("//span[@class=\"headers_custom color_3D5772\"]");
	protected final By MyAis140 = By.xpath("//*[@id=\"navbarSupportedContent\"]/div/ul/li[2]/div/a[1]");
	protected final By SearchBox = By
			.xpath("/html/body/app-root/app-my-ais140-ticket-page/div/div[1]/div[4]/div/div[1]/i/div/input");
	protected final By tableHeadings = By.xpath("//tr[@class=\\\"text-center\\\"]");
	protected final By overlay = By.cssSelector(".overlay");
	protected final By viewButton = By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr/td[12]/mat-icon");
	protected final By Arrow = By.xpath("//div[@class=\"thumb ng-star-inserted\"]");
	protected final By TicketNumber = By.id("mat-input-22");
	protected final By TicketCreate = By.id("mat-input-23");
	protected final By TicketAssigned = By.id("mat-input-24");
	protected final By TicketCompleted = By.id("mat-input-25");
	protected final By TicketCertificate = By.id("mat-input-26");
	protected final By TicketStatus = By.id("mat-input-27");
	protected final By TicketRemark = By.id("mat-input-28");
	protected final By TicketGenrate = By.id("mat-input-29");
	protected final By TicketDesc = By.id("mat-input-30");
	protected final By DeviceInfo = By.className("crm_head_b");
	protected final By UINNumber = By.id("mat-input-31");
	protected final By IMEINumber = By.id("mat-input-32");
	protected final By ICCIDNumber = By.id("mat-input-33");
	protected final By DeviceModel = By.id("mat-input-34");
	protected final By DeviceMake = By.id("mat-input-35");
	protected final By primaryOperatorName = By.id("mat-input-36");
	protected final By primaryOperatorNumber = By.id("mat-input-37");
	protected final By secondaryOperatorName = By.id("mat-input-38");
	protected final By secondaryOperatorNumber = By.id("mat-input-39");
	protected final By vehicleOwnerName = By.id("mat-input-40");
	protected final By vehicleOwnerMobileNumber = By.id("mat-input-41");
	protected final By vehicleOwnerPOAdocname = By.id("mat-input-42");
	protected final By vehicleOwnerPOAdocnumber = By.id("mat-input-43");
	protected final By vehicleOwnerPOIdocname = By.id("mat-input-44");
	protected final By vehicleOwnerPOIdocnumber = By.id("mat-input-45");
	protected final By vehicleOwnerAddress = By.id("mat-input-46");
	protected final By vehiclemodel = By.id("mat-input-47");
	protected final By vehiclemake = By.id("mat-input-48");
	protected final By manufacturingyear = By.id("mat-input-49");
	protected final By chassisnumber = By.id("mat-input-50");
	protected final By enginenumber = By.id("mat-input-51");
	protected final By registrationnumber = By.id("mat-input-52");
	protected final By invoicedate = By.id("mat-input-53");
	protected final By invoicenumber = By.id("mat-input-54");
	protected final By rtostate = By.id("mat-input-55");
	protected final By rtocode = By.id("mat-input-56");
	protected final By ignstatus = By.id("mat-input-57");
	protected final By ignButton = By.xpath(
			"/html/body/app-root/app-my-activations-details-page/div/form/div/div[5]/div/div[2]/div[23]/button");
	protected final By dealercode = By.id("mat-input-58");
	protected final By dealeremail = By.id("mat-input-59");
	protected final By dealercity = By.id("mat-input-60");
	protected final By dealerphoneno = By.id("mat-input-61");
	protected final By posname = By.id("mat-input-62");
	protected final By poscode = By.id("mat-input-63");
	protected final By fotabatchid = By.id("mat-input-64");
	protected final By currentfw = By.id("mat-input-65");
	protected final By assinedfw = By.id("mat-input-66");
	protected final By fotastatus = By.id("mat-input-67");
	protected final By fotaprogress = By.id("mat-input-68");
	protected final By fotapriip = By.id("mat-input-69");
	protected final By fotapriipstatus = By.id("mat-input-70");
	protected final By fotasecip = By.id("mat-input-71");
	protected final By fotasecipstatus = By.id("mat-input-72");
	protected final By stateenableota = By.id("mat-input-73");
	protected final By Reason2skipstage = By.id("mat-select-value-23");
	protected final By Remark2skipstage = By.id("mat-input-75");
	protected final By skippedby = By.id("mat-input-74");
	protected final By selectskipstage2 = By.id("mat-option-190");
	protected final By removestage2restriction = By.xpath(
			"/html/body/app-root/app-my-activations-details-page/div/form/div/div[7]/div/div[2]/div[25]/div[4]/button");
	protected final By VehicleIgnitionOn = By.xpath("//*[@id=\"mat-select-value-1\"]");
	protected final By VehicleIgnitionOnOption = By.xpath("//*[@id=\"mat-option-15\"]/span");
	protected final By Remarkforsubstage1 = By.id("mat-input-2");
	protected final By overallticketstatus = By.xpath("//*[@id=\"mat-select-18\"]/div/div[2]/div");
	protected final By inprogressstatus = By.xpath("//*[@id=\"mat-option-146\"]/span");
	protected final By updatebutton = By.xpath(
			"/html/body/app-root/app-my-activations-details-page/div/form/div/div[8]/div/div[2]/div[61]/button");
	protected final By gsmnw = By.id("mat-select-value-3");
	protected final By gsmnwoption = By.xpath("//*[@id=\"mat-option-33\"]/span");
	protected final By Remarkforsubstage2 = By.id("mat-input-4");
	protected final By gpsfix = By.xpath("//*[@id=\"mat-select-4\"]/div/div[2]/div");
	protected final By gpsfixoption = By.xpath("//*[@id=\"mat-option-51\"]/span");
	protected final By Remarkforsubstage3 = By.id("mat-input-6");
}
