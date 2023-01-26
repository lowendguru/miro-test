# Miro Test

The goal of this project is to demonstrate a test automation approach with a simple framework.


### Assignment requirements and Tips

* Feel free to choose a coding language among Javascript/Typescript/Java. We
  would suggest picking Typescript since our team is using TS as the main E2E UI
  test language
* You could choose any test framework such as selenium, playwright, cypress or testcafe etc.
* Please avoid using some BDD Framework such as Cucumber.
* Please add a Readme within the project
* We can consider a successful registration when the "Check your email" screen is visible.
* The way to manage test structure is also as important as designing the test cases from all possible perspectives.
* We suggest the code of the implemented assignment should not be shared
  publicly. Please host your code within Gitlab and share the project with
  qa.review@miro.com as a “developer” (not as a guest).

# Automated test suite

The functional tests are written in **Java** using **Selenium Webdriver** to drive the browser and **TestNg** to manage test
lifecycle and reporting.

## Structure

### Page Object classes

The Page Object pattern is implemented by mapping pages (or sections) to Java classes where the elements are identified
by web selectors. All page object classes extend from a BasePage and they can be found in
package `src/test/java/pageobjects`.

### Test Classes

The test methods are found in classes with the Test suffix and identified with a @Test annotation. Test classes extend
from a BaseTest class for reusability. Some tests methods are using a data provider and will be executed once for each
test data provided by the data provider. The test classes can be found in package `src/test/java/tests`.

### Configuration file

A properties file is available with configurable values affecting the webdriver like the "headless" option. Also urls,
test data, and other variables can be found in this separate file for
maintenability: `src/test/resources/test.properties`

# Test execution

## Preconditions

The requirements to run the automated tests are:

- Java 11+
- Maven 3.8+
- Chrome latest

# Executing from command line

This is a maven project. If Java and Maven are installed in the host system all dependencies will be automatically
resolved. The test suite can be executed via the following command lines.

To run all tests cases available in the test classes use the command:

```
mvn clean test
```

To run a subset of scenarios filtered by `groups` use the following line with the desired group name:

```
mvn clean test -Dgroups=groupName
```

## Test reports

A full report is available after test execution is completed. The report contains pass/fail data, test duration and
failure reason.

Html report with test results are available in the files `target/surefire-reports/html/index.html`
and `target/surefire-reports/index.html`. These reports contain the same information in different formats.

For demo purposes a generated report can be found in the test server
url: https://lowendguru.com/miro-test/reports/html/suite0_test0_results.html

## Scope

Only English language was considered and the language selection behavior was intentionally left out of scope. This needs to be taken into account while executing the tests because if the system autodetects a language different than English it will result in failed tests.

Extensive step-by-step reporting with screenshot capturing upon test failure was intentionally left out of scope to reduce complexity and maintain simplicity.

## Results

Out of 49 total test cases, 4 are failing. 3 failures related to the test shouldValidateEmailTextFieldSignUpPage happen because of the bug reported here:
https://github.com/lowendguru/miro-test/issues/3
The failure related to test shouldSignUpSuccessfullyFromHomePage is caused by the bug described here:
https://github.com/lowendguru/miro-test/issues/4

Additional bugs were found during the debugging of the automated tests though not necessarily covered by the scope of this suite.

For a full list of found bugs go to:
https://github.com/lowendguru/miro-test/issues


# Video demo of local execution
The following video demonstrates a local execution of the full test suite and the resulting test report.

[![Click for video demo of execution](https://img.youtube.com/vi/Tb_uFNX4rg8/0.jpg)](https://youtu.be/Tb_uFNX4rg8 "Demo video")
