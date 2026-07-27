package com.fastfood.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MenuPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private String openedProductName;
    private int openedProductQuantity = 1;
    private String openedProductId;

    public MenuPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openProduct(String productId, String productName) {
        openedProductId = productId;
        openedProductName = productName;
        openedProductQuantity = 1;
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
        wait.until(ExpectedConditions.invisibilityOfElementLocated(btn));
    }

    public void addToCart(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        System.out.println("[ADD_TO_CART] Requested quantity: " + quantity);
        By increaseQuantityButton = By.id("btn-plus");
        for (int i = 1; i < quantity; i++) {
            System.out.println("[ADD_TO_CART] Preparing click + #" + i);
            logQuantityControls();
            WebElement plusButton = wait.until(
                    ExpectedConditions.elementToBeClickable(increaseQuantityButton)
            );
            System.out.println("[ADD_TO_CART] Clicking candidate: tag=" + plusButton.getTagName()
                    + ", class=" + plusButton.getAttribute("class")
                    + ", text='" + plusButton.getText() + "'");
            plusButton.click();
            System.out.println("[ADD_TO_CART] Click + completed #" + i);
        }

        openedProductQuantity = quantity;
        addToCart();
    }

    private void logQuantityControls() {
        try {
            List<WebElement> modalContents = driver.findElements(
                    By.cssSelector("div.ant-modal-content")
            );
            List<WebElement> circleButtons = driver.findElements(
                    By.cssSelector("div.ant-modal-content button.ant-btn-circle")
            );
            List<WebElement> plusButtons = driver.findElements(
                    By.xpath("//div[contains(@class,'ant-modal-content')]//button[.//span[contains(@class,'anticon-plus')]]")
            );

            System.out.println("[ADD_TO_CART] Modal contents found: " + modalContents.size());
            System.out.println("[ADD_TO_CART] Circle buttons found: " + circleButtons.size());
            System.out.println("[ADD_TO_CART] Plus-icon buttons found: " + plusButtons.size());

            List<WebElement> modalWrappers = driver.findElements(
                    By.cssSelector("div.ant-modal-wrap")
            );
            for (int index = 0; index < modalWrappers.size(); index++) {
                WebElement modal = modalWrappers.get(index);
                System.out.println("[ADD_TO_CART] Modal[" + index + "]"
                        + " displayed=" + modal.isDisplayed()
                        + ", class=" + modal.getAttribute("class")
                        + ", aria-hidden=" + modal.getAttribute("aria-hidden"));
            }

            for (int index = 0; index < circleButtons.size(); index++) {
                WebElement button = circleButtons.get(index);
                System.out.println("[ADD_TO_CART] Button[" + index + "]"
                        + " displayed=" + button.isDisplayed()
                        + ", enabled=" + button.isEnabled()
                        + ", class=" + button.getAttribute("class")
                        + ", aria-label=" + button.getAttribute("aria-label")
                        + ", text='" + button.getText() + "'"
                        + ", html=" + button.getAttribute("outerHTML"));
            }
        } catch (RuntimeException exception) {
            System.out.println("[ADD_TO_CART] Could not inspect quantity controls: "
                    + exception.getClass().getSimpleName() + ": " + exception.getMessage());
        }
    }
    private By cartBadge = By.cssSelector("sup.ant-badge-count");
    private By cartButton = By.cssSelector(".header button");

    public int getCartCount() {
        return Integer.parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText());
    }

    public boolean isOpenedProductInCart() {

        openCart();

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("cart-item-" + openedProductId)
                )
        ).isDisplayed();
    }

    public int getOpenedProductQuantityInCart() {
        openCart();

        WebElement quantity = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("food-quantity-" + openedProductId)
                )
        );
        return Integer.parseInt(quantity.getText());
    }
    private void openCart() {
        if (driver.findElements(getOpenedProductCartItem()).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(cartButton)).click();
        }
    }

    private By getOpenedProductCartItem() {
        return By.id("cart-item-" + openedProductId);
    }

    private String toXPathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return "concat('" + value.replace("'", "', \"'\", '") + "')";
    }
}
