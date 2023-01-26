package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class EmailConfirmationPage extends BasePage {


    By signUpConfirmationLabel = By.cssSelector(".signup .signup__subtitle-form");
    By signUpConfirmationTextField = By.id("code");

    public String getSignUpConfirmationText() {
        waitForJStoLoad();
        WebElement label = getElement(signUpConfirmationLabel);
        logger.info("Sign Up confirmation label text: " + label.getText());
        return label.getText();
    }


}
