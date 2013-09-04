package specs;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * User: Fredde
 * Date: 8/19/13 11:06 PM
 */
@RunWith(ConcordionRunner.class)
public class YakutiaSpecFixtureTest {

    private static String baseUrl;

    static {
        baseUrl = System.getProperty("BASE_URL");
    }

    public String gotoLoginPageAndGetVerifySignInButton() {
        WebDriver driver;
        driver = new FirefoxDriver();
        driver.get(baseUrl);
        WebElement button = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("google2")));
        String returnValue = button.getAttribute("value");
        driver.close();
        return returnValue;
    }

}
