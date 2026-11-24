package mohabTasks.pages;

import java.time.Duration;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class W3SchoolsHtmlTablePage {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);
    private static final By ERNST_HANDEL_COUNTRY = By.xpath(
            "//table[@id='customers']//tr[td[normalize-space()='Ernst Handel']]/td[3]");

    private final WebDriver driver;
    private final String pageUrl;

    public W3SchoolsHtmlTablePage(WebDriver driver, String pageUrl) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        if (pageUrl == null || pageUrl.isBlank()) {
            throw new IllegalArgumentException("W3Schools HTML table URL must not be blank");
        }
        this.pageUrl = pageUrl;
    }

    public void open() {
        driver.get(pageUrl);
    }

    public String getCountryForErnstHandel() {
        WebElement countryCell = new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(ERNST_HANDEL_COUNTRY));
        return countryCell.getText().trim();
    }
}