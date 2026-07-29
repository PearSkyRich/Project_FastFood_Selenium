package com.fastfood.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class KitchenPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public KitchenPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open() {
        driver.get("http://localhost:5173/kitchen");
    }

    /**
     * Đợi bàn xuất hiện trong danh sách bếp
     */
    public void waitUntilTableAppear(String tableNumber) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("kitchen-table-" + tableNumber)
                )
        );
    }

    /**
     * Bấm nút Xong của một món
     */
    public void clickDone(String tableNumber, String foodId) {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("btn-served-" + tableNumber + "-" + foodId)
                )
        ).click();
    }

    /**
     * Đợi bàn biến mất sau khi hoàn thành toàn bộ món
     */
    public void waitUntilTableDisappear(String tableNumber) {

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        By.id("kitchen-table-" + tableNumber)
                )
        );
    }

}