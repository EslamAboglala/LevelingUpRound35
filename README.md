# LevelingUpRound35

LevelingUpRound35 is a Java browser-automation project that uses Selenium WebDriver and TestNG to exercise user-facing web scenarios. It follows the Page Object Model: page classes encapsulate browser interactions, while test classes define the scenarios and assertions.

The test suite covers DuckDuckGo searches and page elements, checkbox interactions, W3Schools table data, file uploads, and jQuery UI drag-and-drop behavior. TestNG suite configuration is in `src/test/resources/TestSuite.xml`.

## Requirements

- Java 25
- Maven
- Google Chrome and Mozilla Firefox for the configured browser tests

## Browser configuration

Set `TargetBrowser` in `src/test/resources/browser.properties` to `chrome` or `firefox`. Chrome is the default for tests that use the configured browser. The Firefox-specific DuckDuckGo test is configured separately in `TestSuite.xml`.

## Optional local Selenium Grid

The tests can run against a single-machine Selenium Grid as an alternative to launching browsers directly. Install Java 25 (Java 11 or newer is required by Selenium Grid) and the browser or browsers you intend to use (Chrome and/or Firefox).

Download the Selenium Server `4.53.0` JAR, which matches the Selenium Java version in `pom.xml`, from the [official Selenium release mirror](https://sourceforge.net/projects/seleniumhq.mirror/files/selenium-4.53.0/selenium-server-4.53.0.jar/download). The published SHA-256 checksum is `3d1d38c7f20db8c96c82e34b83bd1296cd8a3f9eb66073462c7c1b5877634c36`.

From the directory containing the downloaded JAR, start Standalone Grid with Selenium Manager enabled:

```bash
java -jar selenium-server-4.53.0.jar standalone --selenium-manager true
```

When the server is ready, the Grid UI is available at [http://localhost:4444/ui](http://localhost:4444/ui); check [http://localhost:4444/status](http://localhost:4444/status) for a JSON response with top-level `ready` set to `true`. Keep the server running while executing tests.

Grid execution is opt-in. Set `ExecutionMode=grid` and `GridUrl=http://localhost:4444` in `src/test/resources/browser.properties`; `ExecutionMode=local` keeps direct local-driver execution as the default. Then run the suite with `mvn test`.

## Run the tests

Run the TestNG suite with Maven:

```bash
mvn test
```

## GitHub Actions

The `Maven Test Suite` workflow in `.github/workflows/maven-tests.yml` runs the full TestNG suite on every push and pull request, and can also be started manually. It uses Java 25 and runs the local Chrome and Firefox sessions in a virtual display; no Selenium Grid or GitHub secrets are required. Some tests assert live website content, so changes to search results or page titles can cause a test run to fail even when the workflow setup is working.

To trigger a run from the command line, authenticate GitHub CLI once with `gh auth login`, then run this from the repository:

```bash
gh workflow run maven-tests.yml --ref <branch>
gh run list --workflow maven-tests.yml --limit 1
```

Replace `<branch>` with the branch containing the workflow (for example, `main`). The workflow can also be started from the repository’s **Actions** tab by selecting **Maven Test Suite** and clicking **Run workflow**.
