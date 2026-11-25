package mohabtasks.drivers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class BrowserFactory {
    private static final String CONFIGURATION_FILE = "browser.properties";
    private static final String TARGET_BROWSER_PROPERTY = "TargetBrowser";
    private static final String EXECUTION_MODE_PROPERTY = "ExecutionMode";
    private static final String GRID_URL_PROPERTY = "GridUrl";
    private static final String LOCAL_MODE = "local";
    private static final String GRID_MODE = "grid";
    private static final String DEFAULT_GRID_URL = "http://localhost:4444";

    private BrowserFactory() {
    }

    public static WebDriver createDriver() {
        BrowserSettings settings = loadConfiguration();
        return createDriver(settings.targetBrowser(), settings);
    }

    public static WebDriver createDriver(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            throw new IllegalArgumentException("Browser name must not be blank");
        }

        return createDriver(browserName, loadConfiguration());
    }

    private static WebDriver createDriver(String browserName, BrowserSettings settings) {
        String normalizedBrowser = browserName.trim().toLowerCase(Locale.ROOT);
        return switch (normalizedBrowser) {
            case "chrome" -> createDriver(settings, new ChromeOptions(), ChromeDriver::new);
            case "firefox" -> createDriver(settings, new FirefoxOptions(), FirefoxDriver::new);
            case "edge" -> createDriver(settings, new EdgeOptions(), EdgeDriver::new);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };
    }

    private static WebDriver createDriver(BrowserSettings settings, Capabilities options,
                                          Supplier<WebDriver> localDriverSupplier) {
        if (GRID_MODE.equals(settings.executionMode())) {
            RemoteWebDriver remoteDriver = new RemoteWebDriver(toGridUrl(settings.gridUrl()), options);
            remoteDriver.setFileDetector(new LocalFileDetector());
            return remoteDriver;
        }
        return localDriverSupplier.get();
    }

    static String getConfiguredBrowser() {
        return loadConfiguration().targetBrowser();
    }

    static BrowserSettings loadConfiguration() {
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

        return parseConfiguration(properties);
    }

    static BrowserSettings parseConfiguration(Properties properties) {
        String targetBrowser = properties.getProperty(TARGET_BROWSER_PROPERTY);
        if (targetBrowser == null || targetBrowser.isBlank()) {
            throw new IllegalStateException("Missing required property: " + TARGET_BROWSER_PROPERTY);
        }

        String executionMode = properties.getProperty(EXECUTION_MODE_PROPERTY, LOCAL_MODE)
                .trim().toLowerCase(Locale.ROOT);
        if (!Set.of(LOCAL_MODE, GRID_MODE).contains(executionMode)) {
            throw new IllegalStateException("Unsupported execution mode: " + executionMode);
        }

        URI gridUri = null;
        if (GRID_MODE.equals(executionMode)) {
            String gridUrl = properties.getProperty(GRID_URL_PROPERTY, DEFAULT_GRID_URL);
            if (gridUrl == null || gridUrl.isBlank()) {
                throw new IllegalStateException("Missing required property: " + GRID_URL_PROPERTY);
            }
            gridUri = parseGridUri(gridUrl.trim());
        }

        return new BrowserSettings(targetBrowser.trim(), executionMode, gridUri);
    }

    private static URI parseGridUri(String gridUrl) {
        URI gridUri;
        try {
            gridUri = URI.create(gridUrl);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Invalid Grid URL: " + gridUrl, exception);
        }

        String scheme = gridUri.getScheme();
        if (!gridUri.isAbsolute() || scheme == null
                || !Set.of("http", "https").contains(scheme.toLowerCase(Locale.ROOT))
                || gridUri.getHost() == null || gridUri.getUserInfo() != null
                || gridUri.getQuery() != null || gridUri.getFragment() != null) {
            throw new IllegalStateException("Invalid Grid URL: " + gridUrl);
        }
        return gridUri;
    }

    private static URL toGridUrl(URI gridUri) {
        try {
            return gridUri.toURL();
        } catch (MalformedURLException exception) {
            throw new IllegalStateException("Invalid Grid URL: " + gridUri, exception);
        }
    }

    record BrowserSettings(String targetBrowser, String executionMode, URI gridUrl) {
    }
}