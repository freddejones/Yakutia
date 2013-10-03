package specs;

import junit.framework.Assert;
import liquibase.exception.LiquibaseException;
import org.agileinsider.concordion.junit.ConcordionPlus;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.freddejones.yakutia.harness.DriverHandler;
import se.freddejones.yakutia.harness.TestdataManager;

import java.sql.SQLException;

/**
 * User: Fredde
 * Date: 8/19/13 11:06 PM
 */
@RunWith(ConcordionPlus.class)
public class YakutiaSpecAuthenticationFeatureTest {

    private static String baseUrl;
    private DriverHandler dh;

    static {
        baseUrl = System.getProperty("BASE_URL");
    }

    @Before
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
        dh = new DriverHandler();
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

    public String gotoLoginPageAndGetVerifySignInButton() {
        WebDriver driver = dh.getDriver();
        driver.get(baseUrl);
        WebElement button = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("google")));
        String returnValue = button.getAttribute("value");
        return returnValue;
    }

    public String yakutiaSignInGoogleSignInApproveGet() {
        // Sign in when redirected to google
        signInAndApprove(dh);

        // Get header from yakutia create account page
        WebElement header = (new WebDriverWait(dh.getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"overview\"]/div/h1")));
        String headerText = header.getText();
        return headerText;
    }

    public String yakutiaSignInAlreadySignedInApproveGet(){
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
        dh.waitForEnabledElement(accept);
        accept.click();

        // Get header from yakutia create account page
        WebElement header = dh.waitExplicitForElement(By.xpath("//*[@id=\"overview\"]/div/h1"));
        String headerText = header.getText();
        return headerText;
    }

    public String approveCheckPopulatedEmail() throws InterruptedException {
        // Do the sign in
        signInAndApprove(dh);

        WebElement emailElement = dh.waitExplicitForElement(By.name("email"));
        String email = emailElement.getAttribute("value");
        return email;
    }

    public String createAccount() throws LiquibaseException, SQLException, ClassNotFoundException {
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
        return playerName;
    }

    /**
     * Helper methods
     * @param dh
     */
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
