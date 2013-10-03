package se.freddejones.yakutia.harness;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * User: Fredde
 * Date: 9/5/13 8:00 PM
 */
public class DriverHandler {

    private static DriverHandler instance;
    private WebDriver driver;

    public DriverHandler() {
        driver = new FirefoxDriver();
    }

    public void clearCookies() {
        driver.manage().deleteAllCookies();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void closeDriver() {
        driver.close();
    }

    public void resetDriverHandler() {
        instance = null;
    }

    public WebElement waitExplicitForElement(By byType) {
        return (new WebDriverWait(getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(byType));
    }

    public void waitForEnabledElement(WebElement element) {
        for (int i=0; i<10; i++) {
            if (element.isEnabled()) {
                i = 10;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Ugly silence?
                }
            }
        }
    }
}
