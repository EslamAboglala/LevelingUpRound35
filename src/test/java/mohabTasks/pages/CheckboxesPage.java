package mohabTasks.pages;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckboxesPage {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);
    private static final By CHECKBOXES = By.cssSelector("#checkboxes input[type='checkbox']");

    private final WebDriver driver;
    private final String checkboxesUrl;

    public CheckboxesPage(WebDriver driver, String checkboxesUrl) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        if (checkboxesUrl == null || checkboxesUrl.isBlank()) {
            throw new IllegalArgumentException("Checkboxes URL must not be blank");
        }
        this.checkboxesUrl = checkboxesUrl;
    }

    public void open() {
        driver.get(checkboxesUrl);
    }

    public void checkFirstCheckbox() {
        WebElement firstCheckbox = getCheckboxes().getFirst();
        if (!firstCheckbox.isSelected()) {
            firstCheckbox.click();
        }
    }

    public boolean areBothCheckboxesChecked() {
        List<WebElement> checkboxes = getCheckboxes();
        return checkboxes.get(0).isSelected() && checkboxes.get(1).isSelected();
    }

    private List<WebElement> getCheckboxes() {
        return new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.numberOfElementsToBe(CHECKBOXES, 2));
    }
}