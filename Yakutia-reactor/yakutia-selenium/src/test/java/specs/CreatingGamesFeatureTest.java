package specs;

import junit.framework.Assert;
import liquibase.exception.LiquibaseException;
import org.agileinsider.concordion.junit.ConcordionPlus;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.freddejones.yakutia.harness.DriverHandler;
import se.freddejones.yakutia.harness.TestdataManager;
import se.freddejones.yakutia.harness.YakutiaSeleniumBaseTestCase;

import java.sql.SQLException;

/**
 * User: Fredde
 * Date: 9/13/13 10:45 PM
 */
@RunWith(ConcordionPlus.class)
public class CreatingGamesFeatureTest extends YakutiaSeleniumBaseTestCase {

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
    }

    public String gotoCreateGamePageGetH1() throws LiquibaseException, SQLException, ClassNotFoundException {
        TestdataManager.loadTestdataScenario01();
        signInAndApprove(dh);

        // Click in menu on games
        WebElement gamesMenuButton = dh.waitExplicitForElement(By.partialLinkText("Games"));
        gamesMenuButton.click();

        // assert that we have games h1:
        dh.waitExplicitForElement(By.xpath("//*[@id=\"overview\"]/div/h1"));

        // Verify zero created games
        Assert.assertEquals(0, dh.getDriver().findElements(By.xpath("//input[contains(@name,'wmc-current-games:rows')]")).size());

        // Click the create game button
        dh.getDriver().findElement(By.name("createGameButton")).click();

        return dh.waitExplicitForElement(By.xpath("//*[@id=\"overview\"]/div/h1")).getText();
    }

    public String addFriendToListAndGetSize() {
        Assert.assertEquals(1,dh.getDriver().findElements(By.xpath("//div[contains(@id,'playernameAdded')]")).size());
        dh.getDriver().findElement(By.name("wmc-friends:friends:0:addFriendPlayer")).click();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            // No op
        }
        return Integer.toString(dh.getDriver().findElements(By.xpath("//div[contains(@id,'playernameAdded')]")).size());
    }

    public String addGameNameAndClickCreateGameGetGamesSize() {
        WebElement textfield = dh.getDriver().findElement(By.name("gamename"));
        textfield.clear();
        textfield.sendKeys("gamename");

        dh.getDriver().findElement(By.name("submit1")).click();
        Assert.assertEquals("Games",dh.waitExplicitForElement(By.xpath("//*[@id=\"overview\"]/div/h1")).getText());

        return Integer.toString(dh.getDriver().findElements(By.xpath("//input[contains(@name,'wmc-current-games:rows')]")).size());
    }

}
