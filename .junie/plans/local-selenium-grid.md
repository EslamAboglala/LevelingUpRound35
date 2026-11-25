---
sessionId: session-261124-214602-jq73
---

# Requirements

### Goal
Set up a single-machine Selenium Grid, make the existing Maven/TestNG tests optionally run through it for Chrome or Firefox, and provide GitHub Actions automation to execute the suite.

### Scope
- Provide the host-side steps to start and verify Selenium Grid locally.
- Keep current direct browser execution available; make Grid execution selectable by configuration.
- Keep existing Page Objects and test scenarios unchanged except where remote execution requires support.
- Add GitHub Actions execution for the existing Maven/TestNG suite.
- Exclude distributed Grid and Docker orchestration.

# Technical Design

### Current Implementation
`pom.xml` uses Selenium Java `4.53.0` and TestNG; `src/test/resources/TestSuite.xml` runs the configured-browser tests and a Firefox-specific test. `src/test/java/mohabTasks/drivers/BrowserFactory.java` currently creates local drivers, and browser tests call this shared factory. `browser.properties` contains `TargetBrowser=chrome`. `FileUploadPage` sends a local filesystem path to the browser, which needs remote file transfer when the browser runs on Grid.

### Key Decisions
Use Selenium Grid Standalone on the host with the Selenium Server JAR matching `4.53.0`. This is the simplest one-machine setup, needs no Docker, and supports the existing Chrome/Firefox split when those browsers are installed on the host. Keep local execution as the default and route Grid sessions through `RemoteWebDriver` in `BrowserFactory`.

### Proposed Changes
- Extend `browser.properties` with an execution mode and Grid endpoint (default endpoint `http://localhost:4444`).
- Extend `BrowserFactory` to use `ChromeOptions` or `FirefoxOptions` with `RemoteWebDriver` in Grid mode, while preserving the existing local-driver path and browser overrides.
- Set `LocalFileDetector` on Grid `RemoteWebDriver` sessions so the existing upload test can transfer `src/test/resources/images/upload-test.png` to the browser node.
- Document prerequisites and commands in `README.md`: download the Selenium Server `4.53.0` JAR, launch standalone mode with Selenium Manager enabled (`java -jar selenium-server-4.53.0.jar standalone --selenium-manager true`), check `http://localhost:4444/ui` or `/status`, select Grid mode, and run tests with `mvn test`.
- Add a SHA-pinned GitHub Actions workflow for push, pull-request, and manual execution; use Java 25, the existing local Chrome/Firefox setup, and Xvfb on `ubuntu-24.04`.
- Document automatic and manual workflow triggers in `README.md`.

### Risks
Grid mode requires Java and the requested browser(s) on the host; Selenium Manager must be able to resolve compatible drivers. Existing live-site search/title assertions may still fail because their remote pages can return content that differs from the fixed expected values.

# Testing

### Validation Approach
- Add or update `BrowserFactoryTest` coverage for local/Grid configuration validation, unsupported browser names, and a missing/invalid Grid endpoint.
- With the local Grid running, execute a stable browser scenario through Grid in Chrome and Firefox, then exercise the upload scenario to verify remote file transfer.
- Confirm local mode remains usable, the TestNG suite still compiles, and `git diff --check` passes.
- Validate the GitHub Actions YAML and workflow command; note that a hosted workflow run requires pushing the workflow to GitHub.

# Delivery Steps

### ✓ Step 1: Document and start the local Selenium Grid
The machine can launch a standalone Grid server compatible with this project and confirm that it is ready.
- Document the Java/browser prerequisites and Selenium Server `4.53.0` download in `README.md`.
- Include the standalone launch command with Selenium Manager enabled and the Grid UI/status URLs.
- Start the Grid locally and confirm it reports ready at `http://localhost:4444` before connecting tests.

### ✓ Step 2: Add optional Grid sessions to BrowserFactory
Browser tests can choose local drivers or remote Grid sessions from configuration without changing their Page Object flows.
- Add execution-mode and Grid URL settings to `src/test/resources/browser.properties`, retaining local mode as the default.
- Update `src/test/java/mohabTasks/drivers/BrowserFactory.java` to create remote Chrome or Firefox sessions with the existing browser selection and test overrides.
- Apply `LocalFileDetector` to remote sessions so `FileUploadPage` can send its fixture to the browser node.

### ✓ Step 3: Verify Chrome and Firefox Grid execution
The configured test setup successfully creates and closes Grid sessions for both supported browsers, including file upload.
- Extend `BrowserFactoryTest` for configuration validation and invalid Grid settings.
- Run a stable scenario through Grid for Chrome and Firefox, then run `FileUploadTest` to verify remote file transfer.
- Verify the local-driver mode and Maven/TestNG suite compilation remain intact.

### ✓ Step 4: Add GitHub Actions test workflow
The existing TestNG suite can be triggered automatically and manually from GitHub Actions.
- Add `.github/workflows/maven-tests.yml` with push, pull-request, and manual triggers, least-privilege permissions, and per-run concurrency.
- Set up Java 25 and run the complete Maven suite on `ubuntu-24.04` with browser display support.
- Explain automatic and manual execution in `README.md` and validate workflow syntax and the Maven command locally.