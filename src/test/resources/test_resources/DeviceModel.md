# Device Model - UI Test Scenarios

## Setup (`@BeforeClass setUp`)

### Objective
Initialize page objects, reporting utilities, and Excel sheet before executing tests.

### Steps
1. Call `super.setUp()` from `TestBase`.
2. Create `DeviceModelPage` using `driver`.
3. Create `ExcelUtility` instance.
4. Create `SoftAssert` instance.
5. Create `Executor` with `excel` and `softAssert`.
6. Initialize Excel sheet with name: `Device Model`.
7. Log setup completion message.

### Expected Result
- Test environment is initialized successfully.
- `deviceModel`, `excel`, `softAssert`, and `executor` are ready.
- Excel reporting is configured against the `Device Model` sheet.

---

## TC_DM_001 - Validate Active Logged-in Session

### Mapped Automation Method
`testSessionIsActive()` (priority `1`)

### Objective
Verify user session is already active and current page is not redirected to login.

### Preconditions
- Browser is launched through framework setup.
- User authentication/session is already available.

### Test Data
- `Expected`: Current URL should **not** contain `/login` (case-insensitive).

### Steps
1. Read current browser URL using `driver.getCurrentUrl()`.
2. Convert URL to lowercase.
3. Check that URL does not contain `/login`.
4. Record result via executor as `Validate active logged-in session`.

### Expected Result
- Condition evaluates to `true`.
- Test passes when URL does not contain `/login`.

### Negative Scenario
- If URL contains `/login`, session is considered inactive/expired and test should fail.
