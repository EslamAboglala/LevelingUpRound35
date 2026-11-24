package mohabtasks;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import mohabtasks.drivers.BrowserFactory;
import mohabtasks.pages.W3SchoolsHtmlTablePage;

public class W3SchoolsHtmlTableTest {
    private WebDriver driver;
    private W3SchoolsHtmlTablePage w3SchoolsHtmlTablePage;

    @BeforeMethod
    @Parameters("w3SchoolsHtmlTableUrl")
    public void setUp(@Optional("https://www.w3schools.com/html/html_tables.asp") String pageUrl) {
        driver = BrowserFactory.createDriver();
        w3SchoolsHtmlTablePage = new W3SchoolsHtmlTablePage(driver, pageUrl);
    }

    @Test
    public void shouldShowAustriaAsCountryForErnstHandel() {
        w3SchoolsHtmlTablePage.open();
        Assert.assertEquals(w3SchoolsHtmlTablePage.getCountryForErnstHandel(), "Austria",
                "Ernst Handel should have Austria as its country");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}