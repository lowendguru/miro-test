package tests;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pageobjects.BasePage;

import java.lang.invoke.MethodHandles;

import static pageobjects.BasePage.BASE_URL;
import static utils.PropertiesFileReader.getProperty;


public class BaseTest {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static WebDriver driver;

    protected WebDriverWait wait;


    @BeforeMethod(alwaysRun = true)
    protected void setUp() {

        logger.info("Launching webdriver...");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments(getProperty("chromeOptions"));

        if (getProperty("headless").equals("true"))
            options.addArguments("--headless");

        driver = new ChromeDriver(options);

        driver.manage().window().setSize(new Dimension(Integer.parseInt(getProperty("browserWidth")),
                Integer.parseInt(getProperty("browserHeight"))));

        BasePage.driver = driver;
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown() {
        logger.info("Closing webdriver...");
        if (driver != null) {
            driver.quit();
        }
    }

    protected void goToBaseUrl() {
        logger.info("Go to Base URL: " + BASE_URL);
        driver.get(BASE_URL);
    }

}
