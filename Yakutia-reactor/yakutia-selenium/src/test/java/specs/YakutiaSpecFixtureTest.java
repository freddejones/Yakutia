package specs;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * User: Fredde
 * Date: 8/19/13 11:06 PM
 */
@RunWith(ConcordionRunner.class)
public class YakutiaSpecFixtureTest {

    public String gotoLoginPageAndGetVerifySignInButton() {
        WebDriver driver;
        driver = new FirefoxDriver();
        driver.get("http://localhost:8080/yakutia-web");
        WebElement button = driver.findElement(By.id("google2"));
        return button.getText();
    }

}
