package se.jones.code.scenario;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * User: Fredde
 * Date: 6/13/13 10:06 PM
 */
public class SeleniumTest {

    WebDriver driver;
    String baseUrl;

    @Before
    public void setup() {
        driver = new FirefoxDriver();
        System.out.println("baseurl: " + System.getProperty("BASE_URL"));
        baseUrl = System.getProperty("BASE_URL");
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void createNewAccount() throws InterruptedException {

        driver.get(baseUrl);
        WebElement button = driver.findElement(By.id("google2"));

        button.click();

        WebElement email = driver.findElement(By.id("Email"));
        email.sendKeys("yakutia.test@gmail.com");


        WebElement password = driver.findElement(By.id("Passwd"));
        password.sendKeys("testaccount");
        WebElement singInButton = driver.findElement(By.id("signIn"));
        singInButton.click();

        WebElement accept = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("allow")));
        accept.click();

        WebElement newPlayerName = driver.findElement(By.name("playername"));
        newPlayerName.clear();
        newPlayerName.sendKeys("yakutiaName");
        WebElement createButton = driver.findElement(By.name("createButton"));
        createButton.click();

        WebElement header = driver.findElement(By.xpath("//h1[contains(.,'yakutiaName')]"));
        Assert.assertEquals("Welcome to Yakutia, yakutiaName",header.getText());
    }

    @Test
    @Ignore
    public void signIn() {
        googleSignIn();
        loginYakutia();
        WebElement header = driver.findElement(By.xpath("//h1[contains(.,'yakutiaName')]"));
        Assert.assertEquals("Welcome to Yakutia, yakutiaName",header.getText());
    }

    private void loginYakutia() {
        driver.get(baseUrl);
        WebElement button = driver.findElement(By.id("google2"));
        button.click();

        WebElement accept = driver.findElement(By.id("allow"));
        accept.click();
    }

    private void googleSignIn() {
        driver.get("http://accounts.google.com");
        WebElement email = driver.findElement(By.id("Email"));
        email.sendKeys("yakutia.test@gmail.com");
        WebElement password = driver.findElement(By.id("Passwd"));
        password.sendKeys("testaccount");
        WebElement singInButton = driver.findElement(By.id("signIn"));
        singInButton.click();
    }
}
