package com.fastfood.tests;

import com.fastfood.base.BaseTest;
import com.fastfood.pages.LoginPage;
import com.fastfood.pages.MenuPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.AssertJUnit.assertEquals;

public class AddtoCartTest extends BaseTest {

    @Test
    public void addToCart() {
        driver.get("http://localhost:5173/login");

        LoginPage loginPage=new LoginPage(driver);
        MenuPage menuPage=new MenuPage(driver);
        loginPage.login("ban01","a123456");
        System.out.println(
                driver.findElements(By.xpath("//h5")).size()
        );
        menuPage.openProduct("Đùi gà chiên giòn");
        menuPage.addToCart();
        assertEquals(menuPage.getCartCount(), 1);
        driver.quit();

    }
}