package com.aepl.atcu.base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

import com.aepl.atcu.locators.LoginPageLocators;
import com.aepl.atcu.pages.LoginPage;
import com.aepl.atcu.util.MouseActions;
import com.aepl.atcu.util.ConfigProperties;
import com.aepl.atcu.util.Constants;
import com.aepl.atcu.util.WebDriverFactory;


public class TestBase {

	protected static WebDriver driver;
	protected static WebDriverWait wait;
	protected MouseActions action;
	protected LoginPage loginPage;

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@BeforeMethod
	public void setUp() {
		logger.info("========== Test Suite Setup Started ==========");
		if (driver == null) {
			try {
				logger.debug("Initializing properties for QA environment.");
				ConfigProperties.initialize("qa");

				String browserType = ConfigProperties.getProperty("browser").toLowerCase();
				logger.info("Browser configured: {}", browserType);

				WebDriverFactory.setDriver(browserType);
				driver = WebDriverFactory.getWebDriver();
				driver = ensureActiveDriver(browserType);

				if (driver == null) {
					logger.error("WebDriver creation returned null. Aborting setup.");
					throw new RuntimeException("WebDriver initialization failed.");
				}

				wait = new WebDriverWait(driver, Duration.ofSeconds(10));

				driver.manage().window().maximize();

				logger.debug("Navigating to base URL: {}", Constants.BASE_URL);
				try {
					driver.get(Constants.BASE_URL);
				} catch (NoSuchWindowException e) {
					logger.warn("Initial driver window was closed. Reinitializing driver and retrying navigation.", e);
					driver = recreateDriver(browserType);
					wait = new WebDriverWait(driver, Duration.ofSeconds(10));
					driver.manage().window().maximize();
					driver.get(Constants.BASE_URL);
				}

				loginPage = new LoginPage(driver, wait);
				logger.info("Successfully navigated to: {}", Constants.BASE_URL);

				if (!this.getClass().getSimpleName().equals("LoginPageTest")) {
					logger.info("Auto-login initiated for test class: {}", this.getClass().getSimpleName());
					login();
				}

			} catch (Exception e) {
				logger.error("Exception during setup in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
				throw e;
			}
		} else {
			logger.warn("Driver is already initialized. Skipping setup.");
		}
		logger.info("========== Test Suite Setup Completed ==========");
	}

	@BeforeMethod
	public void zoomChrome() {
		if (driver == null) {
			logger.warn("Cannot apply zoom - WebDriver instance is null. Attempting recovery.");
			recoverDriverSession();
		}

		try {
			logger.debug("Zooming out Chrome browser to 67% for test execution.");
			((JavascriptExecutor) driver).executeScript("document.body.style.zoom='67%'");
		} catch (NoSuchWindowException e) {
			logger.warn("Active browser window is closed. Recovering WebDriver session.", e);
			recoverDriverSession();
			((JavascriptExecutor) driver).executeScript("document.body.style.zoom='67%'");
		} catch (Exception e) {
			logger.warn("Zoom action failed once. Attempting session recovery.", e);
			recoverDriverSession();
			((JavascriptExecutor) driver).executeScript("document.body.style.zoom='67%'");
		}
	}

	@AfterSuite
	public void tearDown() {
		logger.info("========== Test Suite Teardown Started ==========");
		if (driver != null) {
			try {
				logger.info("Attempting logout before closing browser.");
				logout();

				driver.quit();
				driver = null;
				logger.info("Browser closed and WebDriver instance reset to null.");
			} catch (Exception e) {
				logger.error("Exception during teardown in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
			}
		} else {
			logger.warn("WebDriver is already null; skipping browser closure.");
		}
		logger.info("========== Test Suite Teardown Completed ==========");
	}

	// ------------------ Helper Methods ------------------

	protected void login() {
		try {
			if (!isLoginPageVisible()) {
				if (isLoggedInUserVisible()) {
					logger.info("Login form not visible. Existing authenticated session detected; skipping login.");
					return;
				}
				logger.warn("Login form and profile both not visible. Navigating to base URL and retrying login.");
				driver.get(Constants.BASE_URL);
				if (!isLoginPageVisible() && isLoggedInUserVisible()) {
					logger.info("Authenticated session available after navigation; skipping login.");
					return;
				}
			}

			logger.debug("Filling login form with credentials.");
			loginPage.enterUsername(ConfigProperties.getProperty("username"))
					.enterPassword(ConfigProperties.getProperty("password")).clickLogin();

			logger.info("Login successful for user: {}", ConfigProperties.getProperty("username"));
		} catch (Exception e) {
			logger.error("Login failed in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
			throw e;
		}
	}

	protected void logout() {
		try {
			logger.debug("Attempting logout action.");
			loginPage.clickLogout();
			logger.info("Logout action completed successfully.");
		} catch (Exception e) {
			logger.error("Logout failed in {}: {}", this.getClass().getSimpleName(), e.getMessage(), e);
			throw e;
		}
	}

	private void recoverDriverSession() {
		try {
			if (driver != null) {
				try {
					driver.quit();
				} catch (Exception ignored) {
					logger.debug("Ignoring exception during stale driver quit.");
				}
				driver = null;
			}

			String browserType = ConfigProperties.getProperty("browser").toLowerCase();
			driver = recreateDriver(browserType);
			wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			driver.manage().window().maximize();
			driver.get(Constants.BASE_URL);
			loginPage = new LoginPage(driver, wait);

			if (!this.getClass().getSimpleName().equals("LoginPageTest")) {
				login();
			}

			logger.info("WebDriver session recovered successfully.");
		} catch (Exception recoveryError) {
			logger.error("Failed to recover WebDriver session.", recoveryError);
			throw recoveryError;
		}
	}

	private WebDriver ensureActiveDriver(String browserType) {
		try {
			driver.getWindowHandle();
			return driver;
		} catch (Exception e) {
			logger.warn("Detected inactive WebDriver session during setup. Recreating browser session.", e);
			return recreateDriver(browserType);
		}
	}

	private WebDriver recreateDriver(String browserType) {
		try {
			if (driver != null) {
				try {
					driver.quit();
				} catch (Exception ignored) {
					logger.debug("Ignoring exception while quitting inactive driver.");
				}
			}
			WebDriverFactory.quitDriver();
			WebDriverFactory.setDriver(browserType);
			WebDriver freshDriver = WebDriverFactory.getWebDriver();
			if (freshDriver == null) {
				throw new RuntimeException("Failed to recreate WebDriver session.");
			}
			return freshDriver;
		} catch (Exception e) {
			logger.error("Driver recreation failed.", e);
			throw e;
		}
	}

	private boolean isLoginPageVisible() {
		try {
			java.util.List<WebElement> elements = driver.findElements(LoginPageLocators.LOGIN_FLD);
			return !elements.isEmpty() && elements.get(0).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isLoggedInUserVisible() {
		try {
			java.util.List<WebElement> elements = driver.findElements(LoginPageLocators.PROFILE_ICON);
			return !elements.isEmpty() && elements.get(0).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
}
