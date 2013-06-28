package nu.danielsundberg.yakutia.scenario;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * User: Fredde
 * Date: 6/13/13 10:06 PM
 */
public class SeleniumTest {

    WebDriver driver;

    @Before
    public void setup() {
        driver = new FirefoxDriver();
    }

    @After
    public void tearDown() {
        driver.quit();

    }

    @Test
    @Ignore
    public void createNewAccount() throws InterruptedException {

        driver.get("localhost:8080/yakutia-web");
        WebElement button = driver.findElement(By.id("google2"));

        button.click();

        WebElement email = driver.findElement(By.id("Email"));
        email.sendKeys("yakutia.test@gmail.com");


        WebElement password = driver.findElement(By.id("Passwd"));
        password.sendKeys("testaccount");
        WebElement singInButton = driver.findElement(By.id("signIn"));
        singInButton.click();

        WebElement accept = driver.findElement(By.id("allow"));
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
        driver.get("localhost:8080/yakutia-web");
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
