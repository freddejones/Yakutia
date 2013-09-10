package specs;

import junit.framework.Assert;
import liquibase.exception.LiquibaseException;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.jones.code.scenario.DriverHandler;
import se.jones.code.scenario.TestdataManager;

import java.sql.SQLException;

/**
 * User: Fredde
 * Date: 8/19/13 11:06 PM
 */
@RunWith(ConcordionRunner.class)
public class YakutiaSpecAuthenticationFeatureTest {

    private static String baseUrl;
    private DriverHandler dh;

    static {
        baseUrl = System.getProperty("BASE_URL");
    }

    public void setup() {
        try {
            TestdataManager.resetAndRebuild();
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (LiquibaseException e) {
            Assert.fail(e.getMessage());
        }
        dh = DriverHandler.getInstance();
    }

    public void tearDown() {
        dh.clearCookies();
        dh.closeDriver();
        dh.resetDriverHandler();
    }

    public String gotoLoginPageAndGetVerifySignInButton() {
        setup();

        WebDriver driver = dh.getDriver();
        driver.get(baseUrl);
        WebElement button = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("google")));
        String returnValue = button.getAttribute("value");
        tearDown();
        return returnValue;
    }

    public String yakutiaSignInGoogleSignInApproveGet() {
        setup();

        // Sign in when redirected to google
        signInAndApprove(dh);

        // Get header from yakutia create account page
        WebElement header = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"overview\"]/div/h1")));
        String headerText = header.getText();
        tearDown();
        return headerText;
    }

    public String yakutiaSignInAlreadySignedInApproveGet(){
        setup();
        dh.getDriver().get("https://accounts.google.com");

        // Google sign in
        WebElement email = dh.waitExplicitForElement(By.id("Email"));
        email.sendKeys("yakutia.test@gmail.com");
        WebElement password = dh.getDriver().findElement(By.id("Passwd"));
        password.sendKeys("testaccount");
        WebElement singInButton = dh.getDriver().findElement(By.name("signIn"));
        singInButton.click();

        // Google sign in
        dh.getDriver().get(baseUrl);
        WebElement button = dh.waitExplicitForElement(By.name("google"));
        button.click();

        // Approve access
        WebElement accept = dh.waitExplicitForElement(By.id("submit_approve_access"));
        DriverHandler.getInstance().waitForEnabledElement(accept);
        accept.click();

        // Get header from yakutia create account page
        WebElement header = dh.waitExplicitForElement(By.xpath("//*[@id=\"overview\"]/div/h1"));
        String headerText = header.getText();
        tearDown();
        return headerText;
    }

    public String approveCheckPopulatedEmail() throws InterruptedException {
        setup();
        // Do the sign in
        signInAndApprove(dh);

        WebElement emailElement = dh.getDriver().findElement(By.name("email"));
        String email = emailElement.getAttribute("value");
        tearDown();
        return email;
    }

    public String createAccount() throws LiquibaseException, SQLException, ClassNotFoundException {
        setup();
        // Do the sign in
        signInAndApprove(dh);

        WebElement newPlayerName = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("playername")));
        newPlayerName.clear();
        newPlayerName.sendKeys("yakutia-tester");
        WebElement createButton = dh.getDriver().findElement(By.name("createButton"));
        createButton.click();

        WebElement header = dh.getDriver().findElement(By.xpath("//h1[contains(.,'yakutia-tester')]"));
        String fullHeader = header.getText();
        String playerName = fullHeader.split(",")[1].replace(" ","");
        tearDown();
        return playerName;
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
        DriverHandler.getInstance().waitForEnabledElement(accept);
        accept.click();
    }
}
