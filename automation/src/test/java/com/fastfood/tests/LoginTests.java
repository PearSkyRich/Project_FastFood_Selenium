package com.fastfood.tests;

import com.fastfood.base.BaseTest;
import com.fastfood.data.LoginDataProvider;
import com.fastfood.model.LoginData;
import com.fastfood.pages.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;


public class LoginTests extends BaseTest {

    @Test(
            dataProvider = "loginSuccessData",
            dataProviderClass = LoginDataProvider.class
    )
    public void loginSuccessTest(LoginData data) {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                data.getUsername(),
                data.getPassword()
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.urlContains(data.getExpectedUrl()));

        Assert.assertTrue(
                driver.getCurrentUrl().contains(data.getExpectedUrl()),
                "Expected URL: " + data.getExpectedUrl()
                        + " | Actual URL: " + driver.getCurrentUrl()
        );
    }

    @Test(
            dataProvider = "loginFailData",
            dataProviderClass = LoginDataProvider.class
    )
    public void loginFailTest(LoginData data) {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                data.getUsername(),
                data.getPassword()
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.urlContains(data.getExpectedUrl()));

        Assert.assertTrue(
                driver.getCurrentUrl().contains(data.getExpectedUrl()),
                "Expected URL: " + data.getExpectedUrl()
                        + " | Actual URL: " + driver.getCurrentUrl()
        );
    }
}