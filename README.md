# LevelingUpRound35

LevelingUpRound35 is a Java browser-automation project that uses Selenium WebDriver and TestNG to exercise user-facing web scenarios. It follows the Page Object Model: page classes encapsulate browser interactions, while test classes define the scenarios and assertions.

The test suite covers DuckDuckGo searches and page elements, checkbox interactions, W3Schools table data, file uploads, and jQuery UI drag-and-drop behavior. TestNG suite configuration is in `src/test/resources/testng.xml`.

## Requirements

- Java 25
- Maven
- Google Chrome and Mozilla Firefox for the configured browser tests

## Browser configuration

Set `TargetBrowser` in `src/test/resources/browser.properties` to `chrome` or `firefox`. Chrome is the default for tests that use the configured browser. The Firefox-specific DuckDuckGo test is configured separately in `testng.xml`.

## Run the tests

Run the TestNG suite with Maven:

```bash
mvn test
```
