package com.fastfood.base;

import com.fastfood.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class MultiUserBaseTest {

    protected WebDriver customer1;
    protected WebDriver customer2;
    protected WebDriver cashier;
    protected WebDriver kitchen;

    @BeforeMethod
    public void setUp() {

        customer1 = DriverFactory.createDriver();
        customer2 = DriverFactory.createDriver();
        cashier = DriverFactory.createDriver();
        kitchen = DriverFactory.createDriver();
    }

    @AfterMethod
    public void tearDown() {


    }
}