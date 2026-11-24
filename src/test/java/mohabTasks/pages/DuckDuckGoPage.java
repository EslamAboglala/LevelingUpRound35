package mohabTasks.pages;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DuckDuckGoPage {
    private static final Duration RESULT_WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final By DUCKDUCKGO_LOGO = By.cssSelector("svg[aria-label='DuckDuckGo']");
    private static final By SEARCH_INPUT = By.name("q");
    private static final By RESULT_TITLE_LINKS = By.cssSelector("a[data-testid='result-title-a']");

    private final WebDriver driver;
    private final String duckDuckGoUrl;

    public DuckDuckGoPage(WebDriver driver, String duckDuckGoUrl) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        if (duckDuckGoUrl == null || duckDuckGoUrl.isBlank()) {
            throw new IllegalArgumentException("DuckDuckGo URL must not be blank");
        }
        this.duckDuckGoUrl = duckDuckGoUrl;
    }

    public void open() {
        driver.get(duckDuckGoUrl);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public boolean isDuckDuckGoLogoDisplayed() {
        return driver.findElements(DUCKDUCKGO_LOGO).stream().anyMatch(WebElement::isDisplayed);
    }

    public void searchFor(String searchQuery) {
        WebElement searchInput = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT));
        searchInput.sendKeys(searchQuery, Keys.ENTER);
    }

    public String getResultLinkUrl(int resultNumber) {
        return getResultTitleLink(resultNumber).getAttribute("href");
    }

    public String getResultTitleText(int resultNumber) {
        return getResultTitleLink(resultNumber).getText();
    }

    private WebElement getResultTitleLink(int resultNumber) {
        if (resultNumber < 1) {
            throw new IllegalArgumentException("Result number must be positive");
        }

        List<WebElement> resultTitleLinks = new WebDriverWait(driver, RESULT_WAIT_TIMEOUT)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(RESULT_TITLE_LINKS, resultNumber - 1));
        return resultTitleLinks.get(resultNumber - 1);
    }
}