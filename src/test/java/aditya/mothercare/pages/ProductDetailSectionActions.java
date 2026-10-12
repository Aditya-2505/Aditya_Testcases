package aditya.mothercare.pages;

import aditya.mothercare.uistore.ProductDetailSection;
import aditya.mothercare.utils.LoggerHandler;
import aditya.mothercare.utils.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Reusable actions for the Product Detail Page (PDP).
 * Also covers pincode / delivery checks.
 */
public class ProductDetailSectionActions {

    private final WebDriver driver;
    private final WebDriverHelper helper;

    public ProductDetailSectionActions(WebDriver driver) {
        this.driver = driver;
        this.helper = new WebDriverHelper(driver);
    }

    // ── Product info ─────────────────────────────────────────────────────────

    public boolean isProductNameVisible() {
        boolean visible = helper.isDisplayed(ProductDetailSection.PRODUCT_NAME);
        LoggerHandler.info("Product name visible: " + visible);
        return visible;
    }

    public String getProductName() {
        return helper.waitForVisible(ProductDetailSection.PRODUCT_NAME).getText().trim();
    }

    public boolean isProductImageVisible() {
        try {
            WebElement img = helper.waitForVisible(ProductDetailSection.PRODUCT_IMAGE);
            // Slider images load lazily — wait up to 10s for naturalWidth > 0
            long deadline = System.currentTimeMillis() + 10_000;
            while (System.currentTimeMillis() < deadline) {
                if (helper.isImageLoaded(img)) {
                    LoggerHandler.info("Product image visible and loaded.");
                    return true;
                }
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                // Re-find in case element went stale during wait
                img = driver.findElement(ProductDetailSection.PRODUCT_IMAGE);
            }
            LoggerHandler.warn("Product image found but not fully loaded after 10s — treating as visible.");
            return img.isDisplayed(); // visible even if naturalWidth not confirmed
        } catch (Exception e) {
            LoggerHandler.error("Product image not found: " + e.getMessage());
            return false;
        }
    }

    public boolean isProductPriceVisible() {
        boolean visible = helper.isDisplayed(ProductDetailSection.PRODUCT_PRICE);
        LoggerHandler.info("Product price visible: " + visible);
        return visible;
    }

    public String getProductPrice() {
        return helper.waitForVisible(ProductDetailSection.PRODUCT_PRICE).getText().trim();
    }

    // ── Quantity selector ────────────────────────────────────────────────────

    public boolean isQuantitySelectorVisible() {
        return helper.isDisplayed(ProductDetailSection.QUANTITY_SELECTOR);
    }

    public int getCurrentQuantity() {
        // Re-find fresh each time — PDP re-renders after qty changes
        String qty = helper.waitForVisible(ProductDetailSection.QUANTITY_DISPLAY).getText().trim();
        LoggerHandler.info("Current quantity displayed: " + qty);
        try {
            return Integer.parseInt(qty);
        } catch (NumberFormatException e) {
            LoggerHandler.error("Quantity is not a number: '" + qty + "'");
            return -1;
        }
    }

    public void clickIncrease() {
        // JS click avoids stale reference after DOM update
        helper.jsClick(ProductDetailSection.INCREASE_BUTTON);
        LoggerHandler.info("Clicked Increase (+) button.");
    }

    public void clickDecrease() {
        helper.jsClick(ProductDetailSection.DECREASE_BUTTON);
        LoggerHandler.info("Clicked Decrease (-) button.");
    }

    // ── Cart and wishlist ────────────────────────────────────────────────────

    public boolean isAddToCartVisible() {
        return helper.isDisplayed(ProductDetailSection.ADD_TO_CART_BUTTON);
    }

    public boolean isAddToWishlistVisible() {
        return helper.isDisplayed(ProductDetailSection.ADD_TO_WISHLIST_BUTTON);
    }

    // ── Pincode / Delivery ───────────────────────────────────────────────────

    public boolean isPincodeFieldVisible() {
        return helper.isDisplayed(ProductDetailSection.PINCODE_INPUT);
    }

    public void enterPincode(String pincode) {
        // Clear existing value, then type the new one
        WebElement input = helper.waitForClickable(ProductDetailSection.PINCODE_INPUT);
        input.clear();
        input.sendKeys(pincode);
        LoggerHandler.info("Entered pincode: " + pincode);
    }

    public void clickCheckButton() {
        dismissOverlayIfPresent();
        try {
            helper.click(ProductDetailSection.CHECK_BUTTON);
        } catch (Exception e) {
            // Overlay may have re-appeared; dismiss again and fall back to JS click
            dismissOverlayIfPresent();
            helper.jsClick(ProductDetailSection.CHECK_BUTTON);
        }
        LoggerHandler.info("Clicked Check button.");
    }

    private void dismissOverlayIfPresent() {
        try {
            // Dynamic Yield modal wrapper that blocks clicks on the page
            By overlay = By.cssSelector("div.dy-modal-wrapper");
            java.util.List<WebElement> overlays = driver.findElements(overlay);
            if (!overlays.isEmpty() && overlays.get(0).isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript(
                    "var el = document.querySelector('div.dy-modal-wrapper'); if(el) el.remove();");
                LoggerHandler.info("Dismissed DY modal overlay.");
            }
        } catch (Exception ignored) {}
    }

    public boolean isDeliveryResultVisible() {
        boolean visible = helper.isDisplayed(ProductDetailSection.DELIVERY_RESULT_CONTAINER);
        LoggerHandler.info("Delivery result container visible: " + visible);
        return visible;
    }

    public String getDeliveryMessage() {
        try {
            return helper.waitForVisible(ProductDetailSection.DELIVERY_AVAILABILITY_MESSAGE).getText().trim();
        } catch (Exception e) {
            LoggerHandler.warn("Delivery message not found: " + e.getMessage());
            return "";
        }
    }

    public boolean isInvalidPincodeAlertVisible() {
        boolean visible = helper.isDisplayed(ProductDetailSection.INVALID_PINCODE_ALERT);
        LoggerHandler.info("Invalid pincode alert visible: " + visible);
        return visible;
    }

    public boolean isPincodeFieldInInvalidState() {
        return helper.isDisplayed(ProductDetailSection.PINCODE_INPUT_INVALID);
    }

    /** Scrolls the pincode section into view before interacting. */
    public void scrollToPincodeField() {
        helper.scrollToElement(ProductDetailSection.PINCODE_INPUT);
    }
}
