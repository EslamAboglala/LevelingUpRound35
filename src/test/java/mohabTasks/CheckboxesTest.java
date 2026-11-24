package mohabtasks;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import mohabtasks.drivers.BrowserFactory;
import mohabtasks.pages.CheckboxesPage;

public class CheckboxesTest {
    private WebDriver driver;
    private CheckboxesPage checkboxesPage;

    @BeforeMethod
    @Parameters("checkboxesUrl")
    public void setUp(@Optional("http://the-internet.herokuapp.com/checkboxes") String checkboxesUrl) {
        driver = BrowserFactory.createDriver();
        checkboxesPage = new CheckboxesPage(driver, checkboxesUrl);
    }

    @Test
    public void shouldCheckBothCheckboxesAfterSelectingTheFirstCheckbox() {
        checkboxesPage.open();
        checkboxesPage.checkFirstCheckbox();
        Assert.assertTrue(checkboxesPage.areBothCheckboxesChecked(), "Both checkboxes should be checked");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}