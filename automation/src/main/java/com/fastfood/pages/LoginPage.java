package com.fastfood.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    private By txtUsername =
            By.cssSelector("input[placeholder='Nhập tên tài khoản']");

    private By txtPassword =
            By.cssSelector("input[placeholder='Nhập mật khẩu']");

    private By btnLogin =
            By.cssSelector("button.btn-login");

    public void enterUsername(String username) {
        driver.findElement(txtUsername).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(txtPassword).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(btnLogin).click();
    }

    public void login(String username, String password) {

        enterUsername(username);

        enterPassword(password);

        clickLogin();
    }

}