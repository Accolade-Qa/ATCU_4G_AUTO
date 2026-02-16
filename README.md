# ATCU 4G Automation

Java + TestNG automation framework for validating the AEPL TCU4G QA Diagnostic Cloud UI and selected backend flows.

## Overview

This project automates major ATCU 4G workflows using Selenium WebDriver (UI) and RestAssured (API helpers), with:

- Page Object Model style structure (`pages`, `locators`, `tests`)
- TestNG-based test orchestration and listeners
- Excel-based per-test logging (`test-results/*.xlsx`)
- Extent HTML reporting (`test-results/ExtentReport.html`)
- Log4j2 logging (`logs/test-automation.log`)

## Tech Stack

- Java 17
- Maven
- Selenium 4
- TestNG 7
- WebDriverManager
- Apache POI
- RestAssured
- ExtentReports
- Log4j2

Primary dependency definitions: `pom.xml`

## Project Structure

```text
ATCU_4G_AUTO/
  src/
    main/java/com/aepl/atcu/
      actions/
      base/
      listeners/
      locators/
      pages/
      util/
    main/resources/
      log4j2.xml
    test/java/com/aepl/atcu/tests/
      LoginPageTest.java
      DeviceDashboardPageTest.java
      DeviceModelPageTest.java
      DealerFotaPageTest.java
      DispatchedDevicePageTest.java
      OtaPageTest.java
      MyAis140TicketsPageTest.java
      Executor.java
  testNG.xml
  GitManager.sh
  pom.xml
```

## Test Modules Currently Present

The following TestNG classes exist in source:

- `LoginPageTest`
- `DeviceDashboardPageTest`
- `MyAis140TicketsPageTest`
- `OtaPageTest`
- `DeviceModelPageTest`
- `DealerFotaPageTest`
- `DispatchedDevicePageTest`

## How Test Execution Works

- `TestBase` initializes WebDriver in `@BeforeClass`.
- Environment properties are loaded via `ConfigProperties.initialize("qa")`.
- Base URL is opened from `Constants.BASE_URL`.
- Non-login test classes auto-login in setup.
- `@BeforeMethod` applies browser zoom (67%) via JavaScript.
- `@AfterSuite` attempts logout and quits the browser.
- `TestListener` captures status to Extent report and screenshots on failure.

## Prerequisites

- JDK 17 installed and configured (`JAVA_HOME`)
- Maven 3.8+
- Chrome/Firefox/Brave installed (as needed)
- Network access to test environment URLs used by the framework

Notes:

- Brave execution uses a hardcoded Windows path in `WebDriverFactory`:
  `C:\Program Files\BraveSoftware\Brave-Browser\Application\brave.exe`
- Chrome/Firefox drivers are auto-managed by WebDriverManager.

## Configuration

The framework reads environment properties from:

- `src/main/resources/<env>.config.properties`

Current setup calls:

- `ConfigProperties.initialize("qa")`

So you must provide:

- `src/main/resources/qa.config.properties`

Based on code usage, properties referenced include keys such as:

- `browser`
- `username`, `password`
- `valid.username`, `valid.password`
- `dashboard.url`
- `ota`, `ota.master`
- `device.model`, `add.device.model`
- `dealer.fota`, `upload.csv`
- `Add.Dispatch.Devices`
- `myTickets.url`

## Build

```bash
mvn clean install
```

## Run Tests

Run all Maven-detected tests:

```bash
mvn test
```

Run a specific class:

```bash
mvn -Dtest=LoginPageTest test
```

Run using TestNG suite XML:

```bash
mvn -DtestngXmlFile=testNG.xml test
```

## Output Artifacts

After execution, check:

- `test-results/*.xlsx` for Excel status logs
- `test-results/ExtentReport.html` for Extent dashboard
- `logs/test-automation.log` for consolidated logs
- `screenshots/` for failure screenshots (created at runtime)
- `test-output/` for TestNG default output

## Utility Script

`GitManager.sh` performs:

- cleanup of generated folders
- `mvn clean install`
- git add/commit/push workflow

Use with caution because it stages all changes (`git add .`).


## Recommended Cleanup (Optional)

To stabilize suite execution, align `testNG.xml` with actual class names/packages currently present in `src/test/java/com/aepl/atcu/tests`.

## Maintainers

Automation Team - AEPL ATCU 4G
