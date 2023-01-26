package pageobjects;

import org.openqa.selenium.By;

import static utils.PropertiesFileReader.getProperty;


public class HomePage extends BasePage {

    public static String URL = BASE_URL + getProperty("homePageUrl");

    By emailTextField = By.cssSelector("[data-testid='button-with-input-cta'] input");

    By emailErrorLabel = By.cssSelector("[data-testid='button-with-input-cta'] div:last-child");
    By signUpButton = By.cssSelector("[data-testid='button-with-input-cta'] a");
    By headerSignUpButton = By.cssSelector("li a[href='/signup/']");

    public void clickHeaderSignupButton() {
        getElement(headerSignUpButton).click();
    }

    public void enterEmail(String email) {
        getElement(emailTextField).clear();
        getElement(emailTextField).sendKeys(email);
    }

    public void clickSignUpButton() {
        getElement(signUpButton).click();
    }

    public boolean isEmailErrorVisible() {
        return getElement(emailErrorLabel).isDisplayed();
    }
}
