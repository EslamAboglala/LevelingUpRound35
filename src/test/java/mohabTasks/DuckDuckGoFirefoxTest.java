package mohabtasks;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import mohabtasks.drivers.BrowserFactory;
import mohabtasks.pages.DuckDuckGoPage;

public class DuckDuckGoFirefoxTest {
    private WebDriver driver;
    private DuckDuckGoPage duckDuckGoPage;

    @BeforeMethod
    @Parameters({"browser", "duckDuckGoUrl"})
    public void setUp(@Optional("firefox") String browserName,
                      @Optional("https://duckduckgo.com/") String duckDuckGoUrl) {
        driver = BrowserFactory.createDriver(browserName);
        duckDuckGoPage = new DuckDuckGoPage(driver, duckDuckGoUrl);
    }

    @Test
    public void shouldShowTestNgTutorialAsFourthSearchResult() {
        duckDuckGoPage.open();
        duckDuckGoPage.searchFor("TestNG");
        Assert.assertEquals(duckDuckGoPage.getResultTitleText(4), "TestNG Tutorial",
                "The fourth search result should be TestNG Tutorial");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}