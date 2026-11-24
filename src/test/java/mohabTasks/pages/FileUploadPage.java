package mohabtasks.pages;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FileUploadPage {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);
    private static final By FILE_INPUT = By.id("file-upload");
    private static final By UPLOAD_BUTTON = By.id("file-submit");
    private static final By UPLOAD_SUCCESS_MESSAGE = By.cssSelector("#content .example h3");
    private static final By UPLOADED_FILE_NAME = By.id("uploaded-files");

    private final WebDriver driver;
    private final String fileUploadUrl;

    public FileUploadPage(WebDriver driver, String fileUploadUrl) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        if (fileUploadUrl == null || fileUploadUrl.isBlank()) {
            throw new IllegalArgumentException("File upload URL must not be blank");
        }
        this.fileUploadUrl = fileUploadUrl;
    }

    public void open() {
        driver.get(fileUploadUrl);
    }

    public void uploadFile(Path filePath) {
        if (filePath == null || !Files.isRegularFile(filePath)) {
            throw new IllegalArgumentException("Upload file must exist: " + filePath);
        }

        WebElement fileInput = new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(FILE_INPUT));
        fileInput.sendKeys(filePath.toAbsolutePath().normalize().toString());

        new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(UPLOAD_BUTTON))
                .click();
    }

    public String getUploadSuccessMessage() {
        return getVisibleText(UPLOAD_SUCCESS_MESSAGE);
    }

    public String getUploadedFileName() {
        return getVisibleText(UPLOADED_FILE_NAME);
    }

    private String getVisibleText(By locator) {
        return new WebDriverWait(driver, WAIT_TIMEOUT).until(webDriver -> {
            try {
                WebElement element = webDriver.findElement(locator);
                if (!element.isDisplayed()) {
                    return null;
                }

                String text = element.getText();
                return text.isBlank() ? null : text;
            } catch (StaleElementReferenceException exception) {
                return null;
            }
        });
    }
}