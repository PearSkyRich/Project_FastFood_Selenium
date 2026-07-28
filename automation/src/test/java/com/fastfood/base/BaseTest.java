package com.fastfood.base;

import com.fastfood.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setup() {

        driver = DriverFactory.createDriver();

        driver.get("http://localhost:5173");
    }

    @AfterMethod
    public void tearDown() {
        if(driver != null)
            driver.quit();

    }

}