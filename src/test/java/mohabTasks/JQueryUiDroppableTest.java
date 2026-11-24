package mohabtasks;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import mohabtasks.drivers.BrowserFactory;
import mohabtasks.pages.JQueryUiDroppablePage;

public class JQueryUiDroppableTest {
    private WebDriver driver;
    private JQueryUiDroppablePage droppablePage;

    @BeforeMethod
    @Parameters("jQueryUiDroppableUrl")
    public void setUp(@Optional("https://jqueryui.com/resources/demos/droppable/default.html") String pageUrl) {
        driver = BrowserFactory.createDriver();
        droppablePage = new JQueryUiDroppablePage(driver, pageUrl);
    }

    @Test
    public void shouldChangeDropTargetTextAfterDraggingItem() {
        droppablePage.open();
        droppablePage.dragItemToTarget();

        Assert.assertEquals(droppablePage.getDropTargetText(), "Dropped!",
                "The drop target should display the successful drop text");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}