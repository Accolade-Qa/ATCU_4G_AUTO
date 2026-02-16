# ATCU 4G Automation

Java + TestNG automation framework for validating AEPL TCU4G QA Diagnostic Cloud (UI + selected API-assisted flows).

## Current Implementation Snapshot

- Framework pattern: Page Object Model + utility-driven actions/assertions
- Test orchestration: TestNG (`testNG.xml`) + Maven Surefire
- Browser setup: `WebDriverFactory` with WebDriverManager
- Shared lifecycle: `TestBase` (`@BeforeClass`, `@BeforeMethod`, `@AfterSuite`)
- Reporting:
  - Extent report via `TestListener`
  - Excel result logging via `ExcelUtility`
  - Screenshots on failures
- Refactored test execution style:
  - `Executor` + `SoftAssert`
  - centralized pass/fail comparison and Excel write

## Tech Stack

- Java 17
- Maven
- Selenium 4.27.0
- TestNG 7.10.2
- RestAssured 5.4.0
- Apache POI 5.2.5
- ExtentReports 5.0.9
- Log4j2 2.20.0

## Project Structure

```text
src/main/java/com/aepl/atcu/
  base/
    TestBase.java
  listeners/
    TestListener.java
    RetryFailedTestListener.java
    Transformer.java
  locators/
    CommonPageLocators.java
    DeviceDashboardPageLocators.java
    LoginPageLocators.java
  pages/
    LoginPage.java
    DeviceDashboardPage.java
    DeviceModelPage.java
    DispachedDevicePage.java
    OtaPage.java
    DealearFotaPage.java
    MyAis140TicketsPage.java
    ChangeMobilePage.java
  util/
    PageActionsUtil.java
    PageAssertionsUtil.java
    TableUtils.java
    FormUtils.java
    RandomGeneratorUtils.java
    MouseActions.java
    CalendarActions.java
    ExcelUtility.java
    ConfigProperties.java
    Constants.java
    Result.java
    ExtentManager.java
    ExtentTestManager.java
    EmailReader.java
    WebDriverFactory.java

src/test/java/com/aepl/atcu/tests/
  Executor.java
  LoginPageTest.java
  DeviceDashboardPageTest.java
  DeviceModelPageTest.java
  DispatchedDevicePageTest.java
  OtaPageTest.java
  DealerFotaPageTest.java
  MyAis140TicketsPageTest.java

testNG.xml
pom.xml
README.md
```

## Test Suite Classes (Active)

Configured in `testNG.xml`:

- `com.aepl.atcu.tests.LoginPageTest`
- `com.aepl.atcu.tests.DeviceDashboardPageTest`
- `com.aepl.atcu.tests.MyAis140TicketsPageTest`
- `com.aepl.atcu.tests.OtaPageTest`
- `com.aepl.atcu.tests.DeviceModelPageTest`
- `com.aepl.atcu.tests.DealerFotaPageTest`
- `com.aepl.atcu.tests.DispatchedDevicePageTest`

## Refactored Test Pattern

Recent test refactors (`DealerFotaPageTest`, `DispatchedDevicePageTest`, `OtaPageTest`, `DeviceModelPageTest`, `MyAis140TicketsPageTest`) follow:

1. Unified `setUp()`:
   - page object init
   - `ExcelUtility` init
   - `SoftAssert` init
   - `Executor` init
2. `executor.executeTest(testName, expected, actualSupplier)` in each test
3. Standardized pass/fail/error logging to Excel
4. Reduced duplicated `try/catch/finally` in test classes

## Base Lifecycle

`TestBase` responsibilities:

- initializes environment using `ConfigProperties.initialize("qa")`
- creates driver and wait
- opens base URL from constants
- auto-login for non-`LoginPageTest` classes
- applies browser zoom (67%) before each test method
- performs logout and quits driver in suite teardown

## How to Run

Run full suite (`testNG.xml` via Surefire):

```bash
mvn test
```

Compile only:

```bash
mvn -DskipTests compile
```

Run single test class:

```bash
mvn -Dtest=DeviceDashboardPageTest test
```

## Outputs

Generated artifacts:

- `test-results/*.xlsx` (per-test result logs)
- `test-results/ExtentReport.html` (extent report)
- `screenshots/` (failure screenshots)
- `logs/` (execution logs)
- `test-output/` (default TestNG output)

## Prerequisites

- JDK 17+
- Maven 3.8+
- Supported browser installed (configured via environment properties)
- Access to target QA environment and APIs required by tests

## Notes

- `pom.xml` is configured with `maven-surefire-plugin` to execute `testNG.xml`.
- API helper flows are used in specific page/test flows (not all suites are pure UI).
- Some legacy page classes still contain large method sets and can be further modularized incrementally.
