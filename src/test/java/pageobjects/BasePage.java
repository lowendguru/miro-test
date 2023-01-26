package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;
import java.time.Duration;

import static utils.PropertiesFileReader.getProperty;


public class BasePage {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String BASE_URL = getProperty("baseUrl");
    private static long waitTimeout = Long.parseLong(getProperty("waitShortTimeout"));

    public static WebDriver driver;

    protected static WebElement getElement(By locator) {
        waitForJStoLoad();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeout));
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return driver.findElement(locator);
        } catch (Exception notPresent) {
            logger.warn("Timeout waiting for Element to be present and visible.");
        }
        return null;
    }

    public static WebElement getElementClickable(By locator) {
        waitForJStoLoad();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeout));
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(locator)));
            return driver.findElement(locator);
        } catch (Exception notClickable) {
            Assert.fail("Timeout waiting for Element to be Clickable.");
        }
        return null;
    }

    public static boolean waitForJStoLoad() {
        return waitForJStoLoad(waitTimeout);
    }

    public static boolean waitForJStoLoad(long timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            try {
                return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
            } catch (Exception e) {
                return true;
            }
        };
        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString().equals("complete");
        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

    public static void typeSlowly(WebElement element, String text) {
        for (String s : text.split("")) {
            element.sendKeys(s);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}