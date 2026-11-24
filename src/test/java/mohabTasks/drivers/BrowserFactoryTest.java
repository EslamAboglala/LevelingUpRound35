package mohabtasks.drivers;

import java.util.Locale;
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
}