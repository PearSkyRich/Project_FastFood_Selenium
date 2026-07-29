package com.fastfood.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PaymentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private By customerPhone = By.id("customer-phone");
    private By paymentButton = By.id("btn-payment");
    private By confirmPaymentButton = By.id("btn-confirm-payment");
    private By releaseTableButton = By.id("btn-release-table");

    public void open() {
        driver.get("http://localhost:5173/payment");
    }

    public void waitUntilTableStatus(String tableNumber, String status) {

        By table = By.id("table-" + tableNumber);

        wait.until(driver -> {

            String current = driver.findElement(table)
                    .getAttribute("data-status");

            System.out.println(tableNumber + " = " + current);

            return status.equals(current);
        });
    }
    public void openTable(String tableNumber) {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("table-" + tableNumber)
                )
        ).click();
    }
    public String getTableStatus(String tableNumber){

        return driver.findElement(
                By.id("table-" + tableNumber)
        ).getAttribute("data-status");
    }
    public void inputCustomerPhone(String phone){

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(customerPhone)
        );

        input.clear();
        input.sendKeys(phone);
    }
    public void selectCash(){

        driver.findElement(
                By.id("payment-method-wrapper")
        ).click();

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'ant-select-item') and text()='Tiền mặt']")
                )
        ).click();
    }
    public void selectTransfer(){

        driver.findElement(
                By.id("payment-method-wrapper")
        ).click();

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'ant-select-item') and text()='Chuyển khoản']")
                )
        ).click();
    }
    public void clickPayment(){

        wait.until(
                ExpectedConditions.elementToBeClickable(paymentButton)
        ).click();
    }
    public void confirmPayment(){

        wait.until(
                ExpectedConditions.elementToBeClickable(confirmPaymentButton)
        ).click();
    }    public void payByCash(){

        clickPayment();
        confirmPayment();
    }
    public void payByTransfer(){

        selectTransfer();
        clickPayment();
        confirmPayment();
    }
    public void releaseTable(){

        wait.until(
                ExpectedConditions.elementToBeClickable(releaseTableButton)
        ).click();
    }
    public void waitUntilEmpty(String tableNumber){

        waitUntilTableStatus(tableNumber,"EMPTY");
    }
}
