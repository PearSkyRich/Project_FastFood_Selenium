package com.fastfood.tests;

import com.fastfood.base.BaseTest;
import com.fastfood.data.LoginDataProvider;
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
    public void loginSuccessTest(String username,
                                 String password,
                                 String expectedUrl) {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains(expectedUrl));

        Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrl));
    }

    @Test(
            dataProvider = "loginFailData",
            dataProviderClass = LoginDataProvider.class
    )
    public void loginFailTest(String username,
                              String password,
                              String expectedUrl) {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains(expectedUrl));

        Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrl));
    }
}