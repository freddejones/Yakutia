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
import se.jones.code.scenario.DriverHandler;

/**
 * User: Fredde
 * Date: 8/19/13 11:06 PM
 */
@RunWith(ConcordionRunner.class)
public class YakutiaSpecAuthenticationFeatureTest {

    private static String baseUrl;

    static {
        baseUrl = System.getProperty("BASE_URL");
    }

    public String gotoLoginPageAndGetVerifySignInButton() {
        DriverHandler.getInstance().clearCookies();
        WebDriver driver = DriverHandler.getInstance().getDriver();
        driver.get(baseUrl);
        WebElement button = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("google2")));
        String returnValue = button.getAttribute("value");
        return returnValue;
    }

    public String yakutiaSignInGoogleSignInApproveGet() {
        DriverHandler dh = DriverHandler.getInstance();
        dh.clearCookies();
        dh.getDriver().get(baseUrl);

        // Yakutia sign in
        WebElement button = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("google2")));
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
        DriverHandler.getInstance().waitForEnabledElement(accept);
        accept.click();

        // Get header from yakutia create account page
        WebElement header = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"overview\"]/div/h1")));
        String headerText = header.getText();
        dh.closeDriver();
        return headerText;
    }

    public String yakutiaSignInAlreadySignedInApproveGet(){
        DriverHandler dh = DriverHandler.getInstance();
        dh.resetDriverHandler();
        dh = DriverHandler.getInstance();
        dh.clearCookies();
        dh.getDriver().get("https://accounts.google.com");

        // Google sign in
        WebElement email = dh.getDriver().findElement(By.id("Email"));
        email.sendKeys("yakutia.test@gmail.com");
        WebElement password = dh.getDriver().findElement(By.id("Passwd"));
        password.sendKeys("testaccount");
        WebElement singInButton = dh.getDriver().findElement(By.id("signIn"));
        singInButton.click();

        // Google sign in
        dh.getDriver().get(baseUrl);
        WebElement button = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("google2")));
        button.click();

        // Approve access
        WebElement accept = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("submit_approve_access")));
        DriverHandler.getInstance().waitForEnabledElement(accept);
        accept.click();

        // Get header from yakutia create account page
        WebElement header = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"overview\"]/div/h1")));
        String headerText = header.getText();
        DriverHandler.getInstance().closeDriver();
        return headerText;
    }

}
