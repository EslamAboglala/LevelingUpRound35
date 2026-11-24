package mohabtasks.drivers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class BrowserFactory {
    private static final String CONFIGURATION_FILE = "browser.properties";
    private static final String TARGET_BROWSER_PROPERTY = "TargetBrowser";

    private BrowserFactory() {
    }

    public static WebDriver createDriver() {
        return createDriver(getConfiguredBrowser());
    }

    public static WebDriver createDriver(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            throw new IllegalArgumentException("Browser name must not be blank");
        }

        return switch (browserName.trim().toLowerCase(Locale.ROOT)) {
            case "chrome" -> new ChromeDriver();
            case "firefox" -> new FirefoxDriver();
            case "edge" -> new EdgeDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };
    }

    static String getConfiguredBrowser() {
        Properties properties = new Properties();
        try (InputStream propertiesStream = BrowserFactory.class.getClassLoader()
                .getResourceAsStream(CONFIGURATION_FILE)) {
            if (propertiesStream == null) {
                throw new IllegalStateException("Browser configuration file not found: " + CONFIGURATION_FILE);
            }
            properties.load(propertiesStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load browser configuration file: " + CONFIGURATION_FILE,
                    exception);
        }

        String targetBrowser = properties.getProperty(TARGET_BROWSER_PROPERTY);
        if (targetBrowser == null || targetBrowser.isBlank()) {
            throw new IllegalStateException("Missing required property: " + TARGET_BROWSER_PROPERTY);
        }
        return targetBrowser.trim();
    }
}