package se.jones.code.scenario;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * User: Fredde
 * Date: 9/5/13 8:00 PM
 */
public class DriverHandler {

    private static DriverHandler instance;
    private WebDriver driver;

    public static synchronized DriverHandler getInstance() {
        if (instance == null) {
            instance = new DriverHandler();
        }


        return instance;
    }

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
