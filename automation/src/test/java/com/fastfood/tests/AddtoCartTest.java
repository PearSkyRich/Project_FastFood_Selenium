package com.fastfood.tests;

import com.fastfood.base.BaseTest;
import com.fastfood.pages.LoginPage;
import com.fastfood.pages.MenuPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class AddtoCartTest extends BaseTest {

    private static final int PRODUCT_QUANTITY = 3;

    @Test
    public void addToCart() {
        driver.get("http://localhost:5173/login");

        LoginPage loginPage=new LoginPage(driver);
        MenuPage menuPage=new MenuPage(driver);
        String user = "Ban01";
        loginPage.login(user,"a123456");
        System.out.println(
                driver.findElements(By.xpath("//h5")).size()
        );
        menuPage.openProduct("H001","Đùi gà chiên giòn");
        menuPage.addToCart(PRODUCT_QUANTITY);
        assertEquals(menuPage.getCartCount(), PRODUCT_QUANTITY);
        assertTrue("Added product should be displayed in the cart", menuPage.isOpenedProductInCart());
        assertEquals(menuPage.getOpenedProductQuantityInCart(), PRODUCT_QUANTITY);
        menuPage.placeOrder();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> menuPage.getCartCount() == 0);
    }
}
