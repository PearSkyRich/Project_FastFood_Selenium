package com.fastfood.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MenuPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public MenuPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openProduct(String productName) {
        By btn = By.xpath(
                "//h5[normalize-space()='" + productName + "']/following-sibling::button"
        );

        wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
    }

    public void addToCart() {
        By btn = By.xpath(
                "//div[contains(@class,'ant-modal')]//button[contains(.,'Thêm vào giỏ')]"
        );

        wait.until(ExpectedConditions.visibilityOfElementLocated(btn));
        wait.until(ExpectedConditions.elementToBeClickable(btn)).click();

        System.out.println("Đã click Thêm vào giỏ");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private By cartBadge = By.cssSelector("sup.ant-badge-count");

    public int getCartCount() {
        return Integer.parseInt(driver.findElement(cartBadge).getText());
    }
}
