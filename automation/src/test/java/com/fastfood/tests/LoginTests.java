package com.fastfood.tests;

import com.fastfood.base.BaseTest;
import com.fastfood.data.LoginDataProvider;
import com.fastfood.model.LoginData;
import com.fastfood.pages.LoginPage;
import com.fastfood.utils.ExcelReport;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTests extends BaseTest {

    private static int testCaseNumber = 1;

    @Test(
            dataProvider = "loginSuccessData",
            dataProviderClass = LoginDataProvider.class
    )
    public void loginSuccessTest(LoginData data) {

        executeLoginTest(
                data,
                "High",
                "Login with valid account"
        );
    }

    @Test(
            dataProvider = "loginFailData",
            dataProviderClass = LoginDataProvider.class
    )
    public void loginFailTest(LoginData data) {
        executeLoginTest(
                data,
                "Medium",
                "Login with invalid account"
        );
    }

    /**
     * Hàm dùng chung cho tất cả Login Test
     */
    private void executeLoginTest(LoginData data,
                                  String priority,
                                  String title) {

        long startTime = System.currentTimeMillis();

        String actualResult = "";
        String status = "FAIL";

        try {

            LoginPage loginPage = new LoginPage(driver);

            loginPage.login(
                    data.getUsername(),
                    data.getPassword()
            );

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(10));

            if (!"/login".equals(data.getExpectedUrl())) {

                // ===== Login thành công =====
                wait.until(ExpectedConditions.urlContains(data.getExpectedUrl()));

                Assert.assertTrue(
                        driver.getCurrentUrl().contains(data.getExpectedUrl())
                );

                actualResult = driver.getCurrentUrl();

            } else {

                // ===== Login thất bại =====

                if (data.getUsername().isBlank() || data.getPassword().isBlank()) {

                    // HTML5 Validation
                    By locator = data.getUsername().isBlank()
                            ? By.cssSelector("input[type='text']")
                            : By.cssSelector("input[type='password']");

                    String validationMessage =
                            driver.findElement(locator)
                                    .getAttribute("validationMessage");

                    Assert.assertFalse(validationMessage.isBlank());

                    actualResult = validationMessage;

                } else {

                    // Backend trả lỗi
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(".error-alert")
                    ));

                    String errorMessage = loginPage.getErrorMessage();

                    Assert.assertFalse(errorMessage.isBlank());

                    actualResult = errorMessage;
                }
            }

            status = "PASS";

        } catch (TimeoutException e) {

            actualResult = "Timeout";

            Assert.fail(e.getMessage());

        } catch (Exception e) {

            actualResult = e.getMessage();

            Assert.fail(e.getMessage());

        } finally {

            long executionTime =
                    System.currentTimeMillis() - startTime;

            ExcelReport.writeResult(

                    String.format("TC_LOGIN_%03d", testCaseNumber++),

                    priority,

                    title,

                    "Enter username, password and click Login",

                    "Username: "
                            + data.getUsername()
                            + " | Password: "
                            + data.getPassword(),

                    data.getExpectedUrl().equals("/login")
                            ? "Display login error message"
                            : "Redirect to " + data.getExpectedUrl(),

                    actualResult,

                    executionTime,

                    status
            );
        }
    }

    /**
     * Lưu Excel sau khi chạy toàn bộ test
     */
    @AfterSuite
    public void saveReport() {

        ExcelReport.saveReport();

    }

}