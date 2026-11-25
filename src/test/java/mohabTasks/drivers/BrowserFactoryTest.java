package mohabtasks.drivers;

import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BrowserFactoryTest {
    @Test
    public void shouldLoadSupportedTargetBrowserFromProperties() {
        String targetBrowser = BrowserFactory.getConfiguredBrowser().toLowerCase(Locale.ROOT);
        Assert.assertTrue(Set.of("chrome", "firefox").contains(targetBrowser),
                "TargetBrowser should be set to Chrome or Firefox");
    }

    @Test
    public void shouldDefaultToLocalExecutionMode() {
        BrowserFactory.BrowserSettings settings = BrowserFactory.parseConfiguration(withChromeBrowser());

        Assert.assertEquals(settings.executionMode(), "local");
        Assert.assertNull(settings.gridUrl());
    }

    @Test
    public void shouldUseDefaultGridUrlWhenNotConfigured() {
        Properties properties = withChromeBrowser();
        properties.setProperty("ExecutionMode", "grid");

        BrowserFactory.BrowserSettings settings = BrowserFactory.parseConfiguration(properties);

        Assert.assertEquals(settings.gridUrl().toString(), "http://localhost:4444");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldRejectMissingGridUrl() {
        Properties properties = withChromeBrowser();
        properties.setProperty("ExecutionMode", "grid");
        properties.setProperty("GridUrl", " ");

        BrowserFactory.parseConfiguration(properties);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldRejectInvalidGridUrl() {
        Properties properties = withChromeBrowser();
        properties.setProperty("ExecutionMode", "grid");
        properties.setProperty("GridUrl", "file:///tmp/selenium-grid");

        BrowserFactory.parseConfiguration(properties);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldRejectUnsupportedBrowser() {
        BrowserFactory.createDriver("unsupported");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldRejectBlankBrowserName() {
        BrowserFactory.createDriver(" ");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldRejectNullBrowserName() {
        BrowserFactory.createDriver(null);
    }

    private Properties withChromeBrowser() {
        Properties properties = new Properties();
        properties.setProperty("TargetBrowser", "chrome");
        return properties;
    }
}