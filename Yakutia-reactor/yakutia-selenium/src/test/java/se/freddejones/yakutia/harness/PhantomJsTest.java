package se.freddejones.yakutia.harness;

import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.core.annotation.Page;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

/**
 * Created by fidde on 2014-02-14.
 */
public class PhantomJsTest {

    @Test
    public void testExecuteTest() throws Exception {
//        googlePage.goTo("http://www.google.com");
//        googlePage.submitSearch();

        DesiredCapabilities caps = new DesiredCapabilities();
        WebDriver driver = new RemoteWebDriver(new URL("http://localhost:6000"), caps);
//        RemoteWebDriver driver = new FirefoxDriver();
        driver.get("http://www.google.com");

        String Logintext = driver.findElement(By.linkText("Maps")).getText();
        System.out.println(Logintext);
    }

}
