package mohabtasks.pages;

import java.time.Duration;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JQueryUiDroppablePage {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);
    private static final By DRAGGABLE = By.id("draggable");
    private static final By DROPPABLE = By.id("droppable");

    private final WebDriver driver;
    private final String pageUrl;

    public JQueryUiDroppablePage(WebDriver driver, String pageUrl) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        if (pageUrl == null || pageUrl.isBlank()) {
            throw new IllegalArgumentException("jQuery UI droppable URL must not be blank");
        }
        this.pageUrl = pageUrl;
    }

    public void open() {
        driver.get(pageUrl);
    }

    public void dragItemToTarget() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        WebElement draggable = wait.until(ExpectedConditions.visibilityOfElementLocated(DRAGGABLE));
        WebElement droppable = wait.until(ExpectedConditions.visibilityOfElementLocated(DROPPABLE));

        new Actions(driver).dragAndDrop(draggable, droppable).perform();
    }

    public String getDropTargetText() {
        return new WebDriverWait(driver, WAIT_TIMEOUT).until(webDriver -> {
            String targetText = webDriver.findElement(DROPPABLE).getText().trim();
            return targetText.equals("Drop here") ? null : targetText;
        });
    }
}