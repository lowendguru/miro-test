package pageobjects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static utils.PropertiesFileReader.getProperty;


public class SignUpPage extends BasePage {

    public static String URL = BASE_URL + getProperty("signUpPageUrl");

    By emailTextField = By.id("email");
    By continueButtons = By.cssSelector(".signup__submit");
    By nameTextField = By.id("name");
    By passwordTextField = By.id("password");
    By termsCheckBox = By.cssSelector(".mr-checkbox-1__wrap label[for='signup-terms']");
    By socialLoginTermsCheckBox = By.cssSelector(".mr-checkbox-1__check[for=tos-signup-terms]");
    By emailErrorLabel = By.id("emailError");
    By emailErrorLabelCustom = By.id("custom-email-error");
    By nameErrorLabel = By.id("nameError");
    By termsErrorLabel = By.id("termsError");
    By termsLink = By.cssSelector("[data-testid=mr-link-terms-1]");
    By privacyLink = By.cssSelector("[data-testid=mr-link-privacy-1]");
    By passwordStrengthLabel = By.id("signup-form-password");
    By passwordErrorLabel = By.id("passwordError");
    By passwordEmptyLabel = By.cssSelector("[data-testid='please-enter-your-password-1']");
    By signUpGoogleButton = By.id("a11y-signup-with-google");
    By signUpSlackButton = By.id("kmq-slack-button");
    By signUpOfficeButton = By.id("kmq-office365-button");
    By signUpAppleButton = By.id("apple-auth");
    By signUpFacebookButton = By.cssSelector("[data-soc=facebook]");
    By socialLoginContinueButton = By.cssSelector("[data-testid=mr-form-gdpr-btn-signin-1]");

    public void clickSocialLoginContinueButton() {
        getElement(socialLoginContinueButton).click();
    }

    public void clickGoogleButton() {
        getElement(signUpGoogleButton).click();
    }

    public void clickSlackButton() {
        getElement(signUpSlackButton).click();
    }

    public void clickOfficeButton() {
        getElement(signUpOfficeButton).click();
    }

    public void clickAppleButton() {
        getElement(signUpAppleButton).click();
    }

    public void clickFacebookButton() {
        getElement(signUpFacebookButton).click();
    }

    public void enterPassword(String password) {
        getElement(passwordTextField).clear();
        getElement(passwordTextField).sendKeys(password);
    }

    public void enterPasswordSlowly(String password) {
        getElement(passwordTextField).clear();
        typeSlowly(getElement(passwordTextField), password);
    }

    public String getPasswordEmptyLabel() {
        return getElement(passwordEmptyLabel).getText();
    }

    public String getPasswordStrengthLabel() {
        return getElement(passwordStrengthLabel).getText();
    }

    public String getPasswordErrorLabel() {
        return getElement(passwordErrorLabel).getText();
    }

    public String getEmailErrorLabelText() {
        return getElement(emailErrorLabel).getText();
    }

    // workaround because there can be multiple elements for Continue button
    private WebElement getContinueButton() {
        for (WebElement e : driver.findElements(continueButtons)) {
            if (e.isDisplayed()) {
                return e;
            }
        }
        return null;
    }

    public void enterName(String randomName) {
        getElement(nameTextField).clear();
        getElement(nameTextField).sendKeys(randomName);
    }

    public void clickContinueButton() {
        getContinueButton().click();
    }

    public void enableTermsCheckBox() {
        enableCheckbox(termsCheckBox);
    }

    public void enableSocialLoginTermsCheckbox() {
        enableCheckbox(socialLoginTermsCheckBox);
    }

    private void enableCheckbox(By locator) {
        WebElement checkbox = getElement(locator);
        if (!checkbox.isSelected())
            checkbox.click();
    }

    public void enterEmail(String email) {
        getElement(emailTextField).clear();
        getElement(emailTextField).sendKeys(email);
    }

    // workaround because there can be multiple email error labels
    public boolean isEmailErrorVisible() {
        if (getElement(emailErrorLabel) != null) {
            return getElement(emailErrorLabel).isDisplayed();
        }
        return getElement(emailErrorLabelCustom).isDisplayed();
    }

    public boolean isNameTextFieldVisible() {
        WebElement element = getElement(nameTextField);
        if (null != element) {
            return getElement(nameTextField).isDisplayed();
        }
        return false;
    }

    public boolean isNameErrorVisible() {
        return getElement(nameErrorLabel).isDisplayed();
    }

    public boolean isTermsErrorVisible() {
        return getElement(termsErrorLabel).isDisplayed();
    }

    public String getTermsErrorLabelText() {
        return getElement(termsErrorLabel).getText();
    }

    public void clickTermsLink() {
        getElement(termsLink).click();
    }

    public void clickPrivacyLink() {
        getElement(privacyLink).click();
    }


}
