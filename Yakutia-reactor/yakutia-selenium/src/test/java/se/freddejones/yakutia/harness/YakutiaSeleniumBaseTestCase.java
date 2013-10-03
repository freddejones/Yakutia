package se.freddejones.yakutia.harness;

import org.agileinsider.concordion.junit.ConcordionPlus;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * User: Fredde
 * Date: 10/3/13 8:54 PM
 */
public abstract class YakutiaSeleniumBaseTestCase {

    protected String baseUrl;
    protected DriverHandler dh;

    @Before
    public void setup1() {
        dh = new DriverHandler();
        baseUrl = System.getProperty("BASE_URL");
    }

    @After
    public void tearDown() {

        if (dh != null) {
            dh.clearCookies();
            dh.closeDriver();
        }

        dh.resetDriverHandler();
        dh = null;
    }

    protected void signInAndApprove(DriverHandler dh) {
        dh.getDriver().get(baseUrl);

        // Yakutia sign in
        WebElement button = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("google")));
        button.click();

        // Google sign in
        WebElement email = dh.getDriver().findElement(By.id("Email"));
        email.sendKeys("yakutia.test@gmail.com");
        WebElement password = dh.getDriver().findElement(By.id("Passwd"));
        password.sendKeys("testaccount");
        WebElement singInButton = dh.getDriver().findElement(By.id("signIn"));
        singInButton.click();

        // Approve access
        WebElement accept = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("submit_approve_access")));
        dh.waitForEnabledElement(accept);
        accept.click();
    }

}
