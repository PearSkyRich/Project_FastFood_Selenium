package com.fastfood.tests;

import com.fastfood.base.MultiUserBaseTest;
import com.fastfood.pages.KitchenPage;
import com.fastfood.pages.LoginPage;
import com.fastfood.pages.MenuPage;
import com.fastfood.pages.PaymentPage;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class MultiUserOrderTest extends MultiUserBaseTest {

    @Test
    public void orderFlowTest() {

        cashier.get("http://localhost:5173/login");
        kitchen.get("http://localhost:5173/login");
        customer1.get("http://localhost:5173/login");
        customer2.get("http://localhost:5173/login");

        LoginPage ban01 = new LoginPage(customer1);
        MenuPage menuPage=new MenuPage(customer1);

        LoginPage ban02 = new LoginPage(customer2);
        MenuPage menuPage02=new MenuPage(customer2);

        LoginPage thuNgan = new LoginPage(cashier);
        LoginPage bep = new LoginPage(kitchen);

        PaymentPage payment = new PaymentPage(cashier);
        KitchenPage kitchenPage = new KitchenPage(kitchen);

        thuNgan.login("ThuNgan", "a123456");
        bep.login("Bep", "a123456");
        ban01.login("Ban01", "a123456");
        ban02.login("Ban02", "a123456");


        menuPage.openProduct("H003","Burger Gà Cay");
        menuPage.addToCart(2);
        menuPage.openCart();
        menuPage.placeOrder();

        menuPage02.openProduct("H002","Cánh gà chiên giòn sốt cay");
        menuPage02.addToCart(3);
        menuPage02.openCart();
        menuPage02.placeOrder();

        payment.open();
        kitchenPage.open();
        // Ban01 đã đặt món

        payment.waitUntilTableStatus("N01","PENDING");
        payment.openTable("N01");
        payment.payByCash();

        // đợi Ban01 xuất hiện
        kitchenPage.waitUntilTableAppear("N01");

        // hoàn thành từng món
        kitchenPage.clickDone("N01", "H003");

        // đợi bàn biến mất nếu chỉ có 1 món
        kitchenPage.waitUntilTableDisappear("N01");

        payment.waitUntilTableStatus("N01","SERVED");
        payment.openTable("N01");
        payment.releaseTable();
        payment.waitUntilEmpty("N01");

        payment.waitUntilTableStatus("N02","PENDING");
        payment.openTable("N02");
        payment.payByCash();

        // đợi Ban01 xuất hiện
        kitchenPage.waitUntilTableAppear("N02");

        // hoàn thành từng món
        kitchenPage.clickDone("N02", "H002");

        // đợi bàn biến mất nếu chỉ có 1 món
        kitchenPage.waitUntilTableDisappear("N02");

        payment.waitUntilTableStatus("N02","SERVED");
        payment.openTable("N02");
        payment.releaseTable();
        payment.waitUntilEmpty("N02");
    }
}