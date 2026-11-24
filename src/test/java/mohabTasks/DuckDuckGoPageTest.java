package mohabTasks;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import mohabTasks.drivers.BrowserFactory;
import mohabTasks.pages.DuckDuckGoPage;

public class DuckDuckGoPageTest {
    private WebDriver driver;
    private DuckDuckGoPage duckDuckGoPage;

    @BeforeMethod
    @Parameters({"browser", "duckDuckGoUrl"})
    public void setUp(@Optional("chrome") String browserName,
                      @Optional("https://duckduckgo.com/") String duckDuckGoUrl) {
        driver = BrowserFactory.createDriver(browserName);
        duckDuckGoPage = new DuckDuckGoPage(driver, duckDuckGoUrl);
    }

    @Test
    public void shouldAssertGooglePageTitle() {
        duckDuckGoPage.open();
        Assert.assertEquals(duckDuckGoPage.getTitle(), "Google");
    }

    @Test
    public void shouldDisplayDuckDuckGoLogo() {
        duckDuckGoPage.open();
        Assert.assertTrue(duckDuckGoPage.isDuckDuckGoLogoDisplayed(), "DuckDuckGo logo should be displayed");
    }

    @Test
    public void shouldShowSeleniumWebDriverDocumentationAsFirstSearchResult() {
        duckDuckGoPage.open();
        duckDuckGoPage.searchFor("Selenium WebDriver");
        Assert.assertEquals(duckDuckGoPage.getResultLinkUrl(1),
                "https://www.selenium.dev/documentation/webdriver/",
                "The first search result should link to Selenium WebDriver documentation");
    }

    @Test
    public void shouldShowLinkedInAsSecondCucumberIoSearchResult() {
        duckDuckGoPage.open();
        duckDuckGoPage.searchFor("Cucumber IO");
        String secondResultLinkUrl = duckDuckGoPage.getResultLinkUrl(2);
        Assert.assertTrue(secondResultLinkUrl.contains("https://www.linkedin.com"),
                "The second Cucumber IO search result should link to LinkedIn, but was " + secondResultLinkUrl);
    }

    @Test
    public void shouldShowTestNgTutorialAsFourthSearchResultInFirefox() {
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
