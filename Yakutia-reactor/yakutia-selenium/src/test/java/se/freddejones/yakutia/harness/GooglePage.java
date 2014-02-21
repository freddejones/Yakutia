package se.freddejones.yakutia.harness;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.net.MalformedURLException;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by fidde on 2014-02-14.
 */
public class GooglePage extends FluentPage {

    FluentWebElement gbqfq;

    @Override
    public void isAt() {
        assertThat(gbqfq.isDisplayed()).isTrue();
    }

    public void submitSearch() {
        fill("#gbqfq").with("tomtestuff").submit("#gbqfb");
    }

    public WebDriver getDefaultDriver() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
//        caps.setBrowserName("phantomjs");
//        caps.setCapability("port","6000");
//        WebDriver driver = new PhantomJSDriver(caps);
//        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:6000"), caps);
//        FirefoxDriver driver = new FirefoxDriver();
        Capabilities capabilities = new DesiredCapabilities();
        DriverService service = PhantomJSDriverService.createDefaultService(capabilities);
        WebDriver driver = new PhantomJSDriver(service, capabilities);
        return driver;
    }
}
