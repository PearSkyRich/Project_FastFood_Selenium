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

    @Test(dataProvider = "loginData",
            dataProviderClass = LoginDataProvider.class)
    public void loginTest(String username,
                          String password,
                          String expectedUrl,
                          boolean expected) {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains(expectedUrl));
        boolean actual = driver.getCurrentUrl().contains(expectedUrl);
        Assert.assertEquals(actual, expected);
    }

}