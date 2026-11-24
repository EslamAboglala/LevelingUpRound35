package mohabTasks;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import mohabTasks.drivers.BrowserFactory;
import mohabTasks.pages.FileUploadPage;

public class FileUploadTest {
    private WebDriver driver;
    private FileUploadPage fileUploadPage;

    @BeforeMethod
    @Parameters({"browser", "fileUploadUrl"})
    public void setUp(@Optional("chrome") String browserName,
                      @Optional("http://the-internet.herokuapp.com/upload") String fileUploadUrl) {
        driver = BrowserFactory.createDriver(browserName);
        fileUploadPage = new FileUploadPage(driver, fileUploadUrl);
    }

    @Test
    @Parameters("uploadImageResourcePath")
    public void shouldUploadImageSuccessfully(
            @Optional("images/upload-test.png") String uploadImageResourcePath) throws URISyntaxException {
        URL imageResource = getClass().getClassLoader().getResource(uploadImageResourcePath);
        Assert.assertNotNull(imageResource, "Upload image test resource should be available");
        Path uploadImagePath = Path.of(imageResource.toURI());

        fileUploadPage.open();
        fileUploadPage.uploadFile(uploadImagePath);

        Assert.assertEquals(fileUploadPage.getUploadSuccessMessage(), "File Uploaded!",
                "The page should confirm that the file was uploaded");
        Assert.assertEquals(fileUploadPage.getUploadedFileName(), uploadImagePath.getFileName().toString(),
                "The uploaded filename should match the test image");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}