package com.aepl.atcu.tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.LoginPage;
import com.aepl.atcu.util.ConfigProperties;
import com.aepl.atcu.util.Constants;
import com.aepl.atcu.util.ExcelUtility;
import com.aepl.atcu.util.PageAssertionsUtil;
import com.aepl.atcu.util.RandomGeneratorUtils;

public class LoginPageTest extends TestBase {

	private LoginPage loginPage;
	private ExcelUtility excelUtility;
	private PageAssertionsUtil comm;
	private SoftAssert softAssert;
	private Executor executor;
	private RandomGeneratorUtils randomGen;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.loginPage = new LoginPage(driver, wait);
		this.comm = new PageAssertionsUtil(driver, wait);
		this.excelUtility = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excelUtility, softAssert);
		this.randomGen = new RandomGeneratorUtils();
		excelUtility.initializeExcel("Login_Page_Test");
	}

	@Test(priority = 1)
	public void testEmptyUsernameWithValidPassword() {

		loginPage.enterUsername(" ").enterPassword(ConfigProperties.getProperty("password")).clickLogin();

		String actualError = loginPage.getEmailFieldErrorMessage();
		Assert.assertEquals(actualError, Constants.blank_input_box_error);
	}

	@Test(priority = 2)
	public void testValidUsernameWithLongInvalidPassword() {

		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword(randomGen.generateRandomString(16))
				.clickLogin();

		String actualToast = loginPage.getToastMessage();
		Assert.assertEquals(actualToast, Constants.toast_error_msg_02);
	}

	@Test(priority = 3)
	public void testValidUsernameWithEmptyPassword() {

		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword(" ").clickLogin();

		String actualError = loginPage.getPasswordFieldErrorMessage();
		Assert.assertEquals(actualError, Constants.blank_input_box_error);
	}

	@Test(priority = 4)
	public void testInvalidUsernameWithValidPassword() {

		loginPage.enterUsername(randomGen.generateRandomEmail()).enterPassword(ConfigProperties.getProperty("password"))
				.clickLogin();

		String actualToast = loginPage.getToastMessage();
		Assert.assertEquals(actualToast, Constants.toast_error_msg_02);
	}

	@Test(priority = 5)
	public void testEmptyUsernameAndEmptyPassword() {

		loginPage.enterUsername(" ").enterPassword(" ").clickLogin();

		String actualEmailError = loginPage.getEmailFieldErrorMessage();
		String actualPasswordError = loginPage.getPasswordFieldErrorMessage();

		Assert.assertEquals(actualEmailError, Constants.blank_input_box_error);
		Assert.assertEquals(actualPasswordError, Constants.blank_input_box_error);
	}

	@Test(priority = 6)
	public void testInvalidUsernameWithInvalidPassword() {

		loginPage.enterUsername(randomGen.generateRandomEmail()).enterPassword(randomGen.generateRandomString(8))
				.clickLogin();

		String actualToast = loginPage.getToastMessage();
		Assert.assertEquals(actualToast, Constants.toast_error_msg_01);
	}

	@Test(priority = 7)
	public void testValidUsernameWithShortPassword() {

		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword("short").clickLogin();

		String actualError = loginPage.getPasswordFieldErrorMessage();
		Assert.assertEquals(actualError, Constants.password_error_msg_02);
	}

	@Test(priority = 8)
	public void testValidUsernameWithWhitespacePassword() {

		loginPage.enterUsername(ConfigProperties.getProperty("username")).enterPassword("       ").clickLogin();

		String actualError = loginPage.getPasswordFieldErrorMessage();
		Assert.assertEquals(actualError, Constants.blank_input_box_error);
	}

	@Test(priority = 9)
	public void testCorrectUrl() {
		executor.executeTest("Test correct url for the {Sampark Cloud}", true, loginPage::isCorrectUrl);
	}

	// Validate that the login container is visible/displayed
	@Test(priority = 10)
	public void testLoginContainerIsDisplayed() {
		executor.executeTest("Test the login container is displayed", true, loginPage::isLoginContainerIsDisplayed);
	}

	// validate that the site name is matched or not
	@Test(priority = 11)
	public void testSiteNameIsMatched() {
		executor.executeTest("Test the site name is matched", "AEPL TCU4G QA Diagnostic Cloud",
				() -> loginPage.siteNameMaching());
	}

	// Validate that the login form container is visible
	@Test(priority = 12)
	public void testLoginFormContainerIsVisible() {
		executor.executeTest("Test the login form container is visible", true, loginPage::isLoginFormContainerVisible);
	}

	// Validate the login form header should be equal to the expected
	@Test(priority = 13)
	public void testHeaderOfLoginFormContainer() {
		executor.executeTest("Test the header of the login form container", "Welcome Back !",
				loginPage::validateLoginFormHeader);
	}

	// Validate the label of the email field is matching or not
	@Test(priority = 14)
	public void testLabelHeaderOfEmail() {
		executor.executeTest("Test the label header of the email field of login form container", "Your Email Address",
				loginPage::validateLabelOfEmailField);
	}

	// Validate that the person icon is present in the email field
	@Test(priority = 15)
	public void testPersonIconInEmailField() {
		executor.executeTest("Test the {person} icon in the email field", true, () -> loginPage.isPersonIconPresent());
	}

	// Validate that the label of password field is matching or not
	@Test(priority = 16)
	public void testLabelHeaderOfPassword() {
		executor.executeTest("Test the label header of the email field of login form container", "Password",
				loginPage::validateLabelOfPasswordField);
	}

	// Validate that the lock icon is present in the password field
	@Test(priority = 17)
	public void testLockIconInPasswordField() {
		executor.executeTest("Test the {Lock} icon in the password field", true, () -> loginPage.isLockIconPresent());
	}

	// Validate that the eye icon is present in the password field
	@Test(priority = 18)
	public void testEyeIconDisplayedInPasswordField() {
		executor.executeTest("Test the {Eye} icon in the password field", true, () -> loginPage.isEyeIconPresent());
	}

	// Validate that the eye icon is enabled in the password field
	@Test(priority = 19)
	public void testEyeIconEnabledInPasswordField() {
		executor.executeTest("Test the {Eye} icon in the password field", true, () -> loginPage.isEyeIconEnabled());
	}

	// Validate - click on the eye icon and see the class changes from the hidden to
	// visible
	@Test(priority = 20)
	public void testClickOnEyeIcon() {
		executor.executeTest("Test the clicking on eye icon in the password field", true, loginPage::isEyeIconClicked);
	}

	// Validate - forgot password link is present and enabled
	@Test(priority = 21)
	public void testPasswordLink() {
		executor.executeTest("Test the forgot password link is present and enabled", true,
				loginPage::isForgotPasswordLinkPresentAndEnabled);
	}

	// @Test(priority = 22)
	public void testForgotPasswordLink() {
		executor.executeTest("Forgot Password Link Test", Constants.EXP_FRGT_PWD_URL, () -> {
			loginPage.clickForgotPassword();
			return driver.getCurrentUrl();
		});
	}

	// @Test(priority = 23)
	public void testInputErrMessage() {
		executor.executeTest("Input Error Message Test", "This field is required and can't be only spaces.",
				loginPage::inputErrMessage);
	}

	// @Test(priority = 24)
	public void testResetPassword() {
		executor.executeTest("Reset Password Test", "Password reset link sent to your email.",
				loginPage::resetPassword);
	}

	@Test(priority = 25)
	public void testCopyright() {
		executor.executeTest("Copyright Verification Test", Constants.EXP_COPYRIGHT_TEXT, comm::checkCopyright);
	}

	@Test(priority = 26)
	public void testVersion() {
		executor.executeTest("Version Verification Test", Constants.EXP_VERSION_TEXT, comm::checkVersion);
	}

	@Test(priority = 27)
	public void loginSuccess() {
		executor.executeTest("Login Success Test", true, () -> {
			loginPage.enterUsername(ConfigProperties.getProperty("username"))
					.enterPassword(ConfigProperties.getProperty("password")).clickLogin();
			return wait.until(ExpectedConditions.urlToBe(Constants.DASH_URL));
		});
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}

}
