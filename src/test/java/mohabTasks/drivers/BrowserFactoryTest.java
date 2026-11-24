package mohabtasks.drivers;

import org.testng.annotations.Test;

public class BrowserFactoryTest {
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