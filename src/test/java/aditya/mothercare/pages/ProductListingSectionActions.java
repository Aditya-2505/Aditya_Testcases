package aditya.mothercare.pages;

import aditya.mothercare.uistore.ProductListingSection;
import aditya.mothercare.utils.LoggerHandler;
import aditya.mothercare.utils.WebDriverHelper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Reusable actions for the Product Listing Page (PLP).
 */
public class ProductListingSectionActions {

    private final WebDriver driver;
    private final WebDriverHelper helper;

    public ProductListingSectionActions(WebDriver driver) {
        this.driver = driver;
        this.helper = new WebDriverHelper(driver);
    }

    // ── Page load check ──────────────────────────────────────────────────────

    public boolean isListingContainerVisible() {
        boolean visible = helper.isDisplayed(ProductListingSection.LISTING_CONTAINER);
        LoggerHandler.info("Listing container visible: " + visible);
        return visible;
    }

    // ── Category heading ─────────────────────────────────────────────────────

    public boolean isCategoryHeadingVisible() {
        try {
            WebElement h2 = helper.waitForVisible(ProductListingSection.CATEGORY_HEADING);
            boolean visible = h2.isDisplayed();
            LoggerHandler.info("PLP category heading: '" + h2.getText() + "', visible: " + visible);
            return visible;
        } catch (Exception e) {
            LoggerHandler.warn("Category heading not found on PLP: " + e.getMessage());
            return false;
        }
    }

    public String getCategoryHeadingText() {
        try {
            return helper.waitForVisible(ProductListingSection.CATEGORY_HEADING).getText().trim().toLowerCase();
        } catch (Exception e) {
            LoggerHandler.warn("Cannot read category heading text: " + e.getMessage());
            return "";
        }
    }

    // ── Product cards ────────────────────────────────────────────────────────

    public List<WebElement> getProductCards() {
        return helper.waitForAllVisible(ProductListingSection.PRODUCT_CARDS);
    }

    public List<WebElement> getProductLinks() {
        return driver.findElements(ProductListingSection.PRODUCT_LINKS);
    }

    public List<WebElement> getProductImages() {
        return driver.findElements(ProductListingSection.PRODUCT_IMAGES);
    }

    public List<WebElement> getProductNames() {
        return driver.findElements(ProductListingSection.PRODUCT_NAMES);
    }

    public boolean areProductsDisplayed() {
        try {
            // Just check presence — don't store element references that can go stale
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.presenceOfElementLocated(ProductListingSection.PRODUCT_LINKS));
            int count = driver.findElements(ProductListingSection.PRODUCT_LINKS).size();
            LoggerHandler.info("Products on PLP: " + count);
            return count > 0;
        } catch (Exception e) {
            LoggerHandler.warn("No products found on PLP: " + e.getMessage());
            return false;
        }
    }

    // ── Click first product ──────────────────────────────────────────────────

    public void clickFirstProduct() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(ProductListingSection.PRODUCT_LINKS));
        // Retry up to 3 times: PLP can re-render between findElement and executeScript
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement firstLink = driver.findElement(ProductListingSection.PRODUCT_LINKS);
                LoggerHandler.info("JS-clicking first product (attempt " + attempt + ").");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstLink);
                return;
            } catch (StaleElementReferenceException e) {
                LoggerHandler.warn("StaleElement on clickFirstProduct attempt " + attempt + ", retrying...");
                if (attempt == 3) throw e;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
    }

    // ── Breadcrumb ───────────────────────────────────────────────────────────

    public boolean isBreadcrumbVisible() {
        return helper.isDisplayed(ProductListingSection.BREADCRUMB);
    }

    public String getCurrentBreadcrumbText() {
        try {
            return helper.waitForVisible(ProductListingSection.BREADCRUMB_CURRENT).getText().trim();
        } catch (Exception e) {
            LoggerHandler.warn("Breadcrumb current category not found: " + e.getMessage());
            return "";
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
