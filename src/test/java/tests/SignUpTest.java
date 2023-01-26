package tests;


import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageobjects.EmailConfirmationPage;
import pageobjects.HomePage;
import pageobjects.SignUpPage;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import static utils.DataGenerator.*;
import static utils.PropertiesFileReader.getProperty;


public class SignUpTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Test(groups = {"positive", "smoke", "home"}, dataProvider = "validEmails")
    void shouldSignUpSuccessfullyFromHomePage(String email) throws InterruptedException {
        goToBaseUrl();

        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.enterEmail(email);
        homePage.clickSignUpButton();

        SignUpPage signUpPage = new SignUpPage();
        completeSignupStepsWithRandomNameAndPassword(signUpPage);

        verifyConfirmationPageResults(email);
    }


    @Test(groups = {"positive", "smoke", "signup"}, dataProvider = "validEmails")
    void shouldSignUpSuccessfullyFromSignUpPage(String email) throws InterruptedException {
        goToBaseUrl();

        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        signUpPage.enterEmail(email);
        signUpPage.clickContinueButton();
        completeSignupStepsWithRandomNameAndPassword(signUpPage);

        verifyConfirmationPageResults(email);
    }


    @Test(groups = {"positive", "signup"})
    void shouldValidateExistingEmail() {
        goToBaseUrl();

        String email = getProperty("existingEmail");
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        signUpPage.enterEmail(email);
        signUpPage.clickContinueButton();

        // workaround because there are 2 possible behaviors and both seem valid
        signUpPage.waitForJStoLoad();
        if (!driver.getCurrentUrl().contains(SignUpPage.URL)) {
            logger.info("Redirected out of Signup");
            Assertions.assertThat(driver.getCurrentUrl())
                    .describedAs("The user is not taken to Login page while using existing Email")
                    .contains("/login");
        } else {
            logger.info("Not redirected out of Signup. Continuing sign up process with existing email");
            completeSignupStepsWithRandomNameAndPassword(signUpPage);
            Assertions.assertThat(signUpPage.getEmailErrorLabelText())
                    .isEqualTo(getProperty("emailErrorLabelText"));
        }
    }

    @Test(groups = {"negative", "home"}, dataProvider = "invalidEmails")
    void shouldValidateEmailTextFieldHomePage(String email) throws InterruptedException {
        goToBaseUrl();

        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.enterEmail(email);
        homePage.clickSignUpButton();

        Assertions.assertThat(homePage.isEmailErrorVisible())
                .describedAs("Email input error message not visible for: " + email)
                .isTrue();
    }

    @Test(groups = {"negative", "signup"}, dataProvider = "invalidEmails")
    void shouldValidateEmailTextFieldSignUpPage(String email) throws InterruptedException {
        goToBaseUrl();

        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        signUpPage.enterEmail(email);

        Assertions.assertThat(signUpPage.isEmailErrorVisible())
                .describedAs("Email input error message not visible: " + email)
                .isTrue();

        signUpPage.clickContinueButton();
        Assertions.assertThat(signUpPage.isNameTextFieldVisible())
                .describedAs("User should not be able to continue on an invalid email: " + email)
                .isFalse();
    }

    @Test(groups = {"negative", "signup"})
    void shouldValidateUsernameField() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        signUpPage.enterEmail(email);
        signUpPage.clickContinueButton();
        signUpPage.enterName("");
        signUpPage.clickContinueButton();

        Assertions.assertThat(signUpPage.isNameErrorVisible())
                .describedAs("Name input error message not visible")
                .isTrue();

        signUpPage.clickContinueButton();
        Assertions.assertThat(signUpPage.isNameTextFieldVisible())
                .describedAs("User should not be able to continue on an invalid name")
                .isTrue();
    }


    @Test(groups = {"negative", "signup"})
    void shouldValidateTermsAndPrivacyCheckbox() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        completeSignUpStepsWithValidName(email, signUpPage);
        signUpPage.enterPassword(getRandomValidPassword());
        signUpPage.clickContinueButton();
        Assertions.assertThat(signUpPage.isTermsErrorVisible())
                .describedAs("Terms checkbox error label is not displayed")
                .isTrue();
        Assertions.assertThat(signUpPage.getTermsErrorLabelText())
                .describedAs("Terms error text does not match expected")
                .isEqualTo(getProperty("termsErrorLabelText"));
    }


    @Test(groups = {"negative", "signup", "password"})
    void shouldValidatePasswordField() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        completeSignUpStepsWithValidName(email, signUpPage);
        signUpPage.enableTermsCheckBox();
        // no password
        signUpPage.clickContinueButton();
        Assertions.assertThat(signUpPage.getPasswordEmptyLabel())
                .isEqualTo(getProperty("passwordErrorEmpty"));
        // too short
        signUpPage.enterPasswordSlowly(getStringOfLength(5));
        signUpPage.clickContinueButton();
        Assertions.assertThat(signUpPage.getPasswordStrengthLabel())
                .isEqualTo(getProperty("passwordStrengthTooShort"));
        // weak
        signUpPage.enterPasswordSlowly(getProperty("weakPassword"));
        Assertions.assertThat(signUpPage.getPasswordStrengthLabel())
                .isEqualTo(getProperty("passwordStrengthWeak"));
        // so so
        signUpPage.enterPasswordSlowly(getProperty("soSoPassword"));
        Assertions.assertThat(signUpPage.getPasswordStrengthLabel())
                .isEqualTo(getProperty("passwordStrengthSoSo"));
        // good
        signUpPage.enterPasswordSlowly(getProperty("goodPassword"));
        Assertions.assertThat(signUpPage.getPasswordStrengthLabel())
                .isEqualTo(getProperty("passwordStrengthGood"));
        // great
        signUpPage.enterPasswordSlowly(getStringOfLength(15));
        Assertions.assertThat(signUpPage.getPasswordStrengthLabel())
                .isEqualTo(getProperty("passwordStrengthGreat"));
        // too long
        signUpPage.enterPassword(getStringOfLength(61));
        signUpPage.clickContinueButton();
        Assertions.assertThat(signUpPage.getPasswordErrorLabel())
                .isEqualTo(getProperty("passwordErrorTooLong"));
    }

    @Test(groups = {"positive", "signup", "redirection"})
    void shouldRedirectToTermsPage() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        completeSignUpStepsWithValidName(email, signUpPage);
        signUpPage.clickTermsLink();
        // switch to next tab
        ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        Assertions.assertThat(driver.getCurrentUrl())
                .describedAs("Did not redirect to Terms URL")
                .contains(getProperty("termsUrl"));
    }

    @Test(groups = {"positive", "signup", "redirection"})
    void shouldRedirectToPrivacyPage() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        completeSignUpStepsWithValidName(email, signUpPage);
        signUpPage.clickPrivacyLink();
        // switch to next tab
        ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        Assertions.assertThat(driver.getCurrentUrl())
                .describedAs("Did not redirect to Privacy URL")
                .contains(getProperty("privacyUrl"));
    }

    @Test(groups = {"positive", "signup", "redirection"})
    void shouldRedirectToGoogleSignUp() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        signUpPage.clickGoogleButton();
        signUpPage.enableSocialLoginTermsCheckbox();
        signUpPage.clickSocialLoginContinueButton();
        Assertions.assertThat(driver.getCurrentUrl())
                .describedAs("Did not redirect to Google URL")
                .contains(getProperty("socialLoginGoogleUrl"));
    }

    @Test(groups = {"positive", "signup", "redirection"})
    void shouldRedirectToSlackSignUp() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        signUpPage.clickSlackButton();
        signUpPage.enableSocialLoginTermsCheckbox();
        signUpPage.clickSocialLoginContinueButton();
        Assertions.assertThat(driver.getCurrentUrl())
                .describedAs("Did not redirect to Slack URL")
                .contains(getProperty("socialLoginSlackUrl"));
    }

    @Test(groups = {"positive", "signup", "redirection"})
    void shouldRedirectToOfficeSignUp() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        signUpPage.clickOfficeButton();
        signUpPage.enableSocialLoginTermsCheckbox();
        signUpPage.clickSocialLoginContinueButton();
        Assertions.assertThat(driver.getCurrentUrl())
                .describedAs("Did not redirect to Office URL")
                .contains(getProperty("socialLoginOfficeUrl"));
    }

    @Test(groups = {"positive", "signup", "redirection"})
    void shouldRedirectToAppleSignUp() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        signUpPage.clickAppleButton();
        signUpPage.enableSocialLoginTermsCheckbox();
        signUpPage.clickSocialLoginContinueButton();
        Assertions.assertThat(driver.getCurrentUrl())
                .describedAs("Did not redirect to Office URL")
                .contains(getProperty("socialLoginAppleUrl"));
    }

    @Test(groups = {"positive", "signup", "redirection"})
    void shouldRedirectToFacebookSignUp() {
        goToBaseUrl();

        String email = getRandomEmail();
        logger.info("Using Email: " + email);
        HomePage homePage = new HomePage();
        homePage.clickHeaderSignupButton();

        SignUpPage signUpPage = new SignUpPage();
        signUpPage.clickFacebookButton();
        signUpPage.enableSocialLoginTermsCheckbox();
        signUpPage.clickSocialLoginContinueButton();
        Assertions.assertThat(driver.getCurrentUrl())
                .describedAs("Did not redirect to Office URL")
                .contains(getProperty("socialLoginFacebookUrl"));
    }

    private void completeSignupStepsWithRandomNameAndPassword(SignUpPage signUpPage) {
        signUpPage.enterName(getRandomName());
        signUpPage.clickContinueButton();
        signUpPage.enterPassword(getRandomValidPassword());
        signUpPage.enableTermsCheckBox();
        signUpPage.clickContinueButton();
    }

    private void verifyConfirmationPageResults(String email) {
        EmailConfirmationPage emailConfirmationPage = new EmailConfirmationPage();
        Assertions.assertThat(emailConfirmationPage.getSignUpConfirmationText())
                .describedAs("The Sign Up confirmation message does not contain the expected email.")
                .containsIgnoringCase(email);
    }


    private void completeSignUpStepsWithValidName(String email, SignUpPage signUpPage) {
        signUpPage.enterEmail(email);
        signUpPage.clickContinueButton();
        signUpPage.enterName(getRandomName());
        signUpPage.clickContinueButton();
    }

    @DataProvider(name = "validEmails")
    public Object[][] validEmails() {
        return new Object[][]{
                {getRandomEmail()},
                {RandomStringUtils.randomAlphabetic(15) + "@subdomain.domain.com"},
                {RandomStringUtils.randomAlphabetic(15) + "@domain.co.jp"},
                {RandomStringUtils.randomAlphabetic(15) + "@subdomain.domain.co.jp"},
                {RandomStringUtils.randomAlphabetic(7) + "+" + RandomStringUtils.randomAlphabetic(7) + "@" + RandomStringUtils.randomAlphabetic(7) + ".com"},
                {RandomStringUtils.randomAlphabetic(5) + "-" + RandomStringUtils.randomAlphabetic(5) + "_" + RandomStringUtils.randomAlphabetic(5) + "@" + RandomStringUtils.randomAlphabetic(5) + "-" + RandomStringUtils.randomAlphabetic(5) + ".com"},
        };
    }

    @DataProvider(name = "invalidEmails")
    public Object[][] invalidEmails() {
        return new Object[][]{
                {RandomStringUtils.randomAlphabetic(5)},
                {RandomStringUtils.randomAlphabetic(5) + "@"},
                {RandomStringUtils.randomAlphabetic(5) + "@.com"},
                {RandomStringUtils.randomAlphabetic(5) + "@domain"},
                {RandomStringUtils.randomAlphabetic(5) + "domain.com"},
                {RandomStringUtils.randomAlphabetic(5) + "@domain."},
                {RandomStringUtils.randomAlphabetic(5) + "@domain@domain.com"},
                {"." + RandomStringUtils.randomAlphabetic(5) + "@domain.com"},
                {RandomStringUtils.randomAlphabetic(5) + "." + "@domain.com"},
                {"<mailto:email@domain.com>"},
                {"あいうえお" + "@" + RandomStringUtils.randomAlphabetic(5) + ".com"},
                {"email@domain.com (Joe Smith)"},
                {RandomStringUtils.randomAlphabetic(5) + "@domain..com"},
        };
    }

}
