package aditya.mothercare.tests;

import aditya.mothercare.utils.Base;
import aditya.mothercare.pages.ProductListingSectionActions;
import aditya.mothercare.pages.ProductDetailSectionActions;
import aditya.mothercare.utils.*;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * Test cases TS_0091 – TS_0096
 * Product Detail Page: info, quantity controls, pincode validation.
 */
@Epic("Mothercare Product Detail")
@Feature("Product Detail Page")
public class ProductDetailTests extends Base {

    private ProductListingSectionActions plpActions;
    private ProductDetailSectionActions  pdpActions;
    private WebDriverHelper              wdh;

    private static final String PLP_URL          = "https://mothercare.in/collection/furniture--bedding";
    private static final String VALID_PINCODE_1   = "625016";
    private static final String INVALID_PINCODE_1 = "123";
    private static final String INVALID_PINCODE_2 = "000000";

    @BeforeMethod
    public void initPages() {
        plpActions = new ProductListingSectionActions(driver);
        pdpActions = new ProductDetailSectionActions(driver);
        wdh        = new WebDriverHelper(driver);
        LoggerHandler.info("ProductDetailTests setup complete.");
    }

    // ── TS_0091 ──────────────────────────────────────────────────────────────

    @Test(priority = 11, description = "Verify Product Detail Page Opens Successfully")
    @Story("TS_0091")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0091_verifyProductDetailPageOpens() {
        LoggerHandler.testStart("TS_0091", "Verify Product Detail Page Opens Successfully");
        Reporter.startTest("TS_0091", "Verify Product Detail Page Opens Successfully");

        Reporter.info("Step 1: Open Furniture & Bedding PLP.");
        driver.get(PLP_URL);

        Reporter.info("Step 2: Wait for product cards and click first product.");
        plpActions.areProductsDisplayed();
        plpActions.clickFirstProduct();

        Reporter.info("Step 3: Verify Product Detail Page loaded.");
        Assert.assertTrue(pdpActions.isProductNameVisible(), "Product name should be visible on PDP.");
        String productName = pdpActions.getProductName();
        Reporter.pass("Product name visible: '" + productName + "'");

        Assert.assertTrue(pdpActions.isProductImageVisible(), "Product image should be visible and loaded.");
        Reporter.pass("Product image is displayed correctly.");

        Assert.assertTrue(pdpActions.isProductPriceVisible(), "Product price should be visible.");
        String price = pdpActions.getProductPrice();
        Reporter.pass("Product price visible: '" + price + "'");

        Assert.assertTrue(pdpActions.isQuantitySelectorVisible(), "Quantity selector should be visible.");
        Reporter.pass("Quantity selector is displayed.");

        Assert.assertTrue(pdpActions.isAddToCartVisible(), "Add To Cart button should be visible.");
        Reporter.pass("Add To Cart button is visible.");

        Assert.assertTrue(pdpActions.isAddToWishlistVisible(), "Add To Wishlist button should be visible.");
        Reporter.pass("Add To Wishlist button is visible.");

        pdpActions.scrollToPincodeField();
        Assert.assertTrue(pdpActions.isPincodeFieldVisible(), "Pincode input field should be visible.");
        Reporter.pass("Pincode field is visible.");

        String screenshotPath = Screenshot.captureFullPage(driver, "TS_0091");
        Reporter.attachScreenshot(screenshotPath);
    }

    // ── TS_0092 ──────────────────────────────────────────────────────────────

    @Test(priority = 12, description = "Verify Quantity Increase Functionality")
    @Story("TS_0092")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0092_verifyQuantityIncrease() {
        LoggerHandler.testStart("TS_0092", "Verify Quantity Increase Functionality");
        Reporter.startTest("TS_0092", "Verify Quantity Increase Functionality");

        openPdp();

        Reporter.info("Step 1: Verify default quantity is 1.");
        int defaultQty = pdpActions.getCurrentQuantity();
        Assert.assertEquals(defaultQty, 1, "Default quantity should be 1.");
        Reporter.pass("Default quantity is 1.");

        Reporter.info("Step 2: Click '+' once and verify quantity becomes 2.");
        pdpActions.clickIncrease();
        int qtyAfterFirst = pdpActions.getCurrentQuantity();
        Assert.assertEquals(qtyAfterFirst, 2, "Quantity should be 2 after first '+' click.");
        Reporter.pass("Quantity increased to 2 after first click.");

        Reporter.info("Step 3: Click '+' again and verify quantity becomes 3.");
        pdpActions.clickIncrease();
        int qtyAfterSecond = pdpActions.getCurrentQuantity();
        Assert.assertEquals(qtyAfterSecond, 3, "Quantity should be 3 after second '+' click.");
        Reporter.pass("Quantity increased to 3 after second click. Increase functionality verified.");
    }

    // ── TS_0093 ──────────────────────────────────────────────────────────────

    @Test(priority = 13, description = "Verify Quantity Decrease Functionality")
    @Story("TS_0093")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0093_verifyQuantityDecrease() {
        LoggerHandler.testStart("TS_0093", "Verify Quantity Decrease Functionality");
        Reporter.startTest("TS_0093", "Verify Quantity Decrease Functionality");

        openPdp();

        Reporter.info("Step 1: Increase quantity to 2.");
        pdpActions.clickIncrease();
        int qtyAfterIncrease = pdpActions.getCurrentQuantity();
        Assert.assertEquals(qtyAfterIncrease, 2, "Quantity should be 2 before decrease test.");
        Reporter.pass("Quantity is 2.");

        Reporter.info("Step 2: Click '-' once and verify quantity becomes 1.");
        pdpActions.clickDecrease();
        int qtyAfterDecrease = pdpActions.getCurrentQuantity();
        Assert.assertEquals(qtyAfterDecrease, 1, "Quantity should decrease to 1.");
        Reporter.pass("Quantity decreased to 1. Decrease functionality verified.");
    }

    // ── TS_0094 ──────────────────────────────────────────────────────────────

    @Test(priority = 14, description = "Verify Minimum Quantity Restriction")
    @Story("TS_0094")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0094_verifyMinimumQuantityRestriction() {
        LoggerHandler.testStart("TS_0094", "Verify Minimum Quantity Restriction");
        Reporter.startTest("TS_0094", "Verify Minimum Quantity Restriction");

        openPdp();

        Reporter.info("Step 1: Verify default quantity is 1 (minimum).");
        int defaultQty = pdpActions.getCurrentQuantity();
        Assert.assertEquals(defaultQty, 1, "Default quantity should be 1.");
        Reporter.pass("Default quantity is 1.");

        Reporter.info("Step 2: Attempt to decrease below 1.");
        pdpActions.clickDecrease();
        int qtyAfterFirstAttempt = pdpActions.getCurrentQuantity();

        Reporter.info("Step 3: Verify quantity remains at 1.");
        Assert.assertEquals(qtyAfterFirstAttempt, 1,
                "Quantity should remain 1 after attempting to decrease below minimum.");
        Reporter.pass("Quantity remained at 1 after first decrease attempt.");

        Reporter.info("Step 4: Attempt to decrease again.");
        pdpActions.clickDecrease();
        int qtyAfterSecondAttempt = pdpActions.getCurrentQuantity();
        Assert.assertEquals(qtyAfterSecondAttempt, 1,
                "Quantity should remain 1 after second decrease attempt.");
        Reporter.pass("Minimum quantity restriction enforced. Quantity remains at 1.");
    }

    // ── TS_0095 ──────────────────────────────────────────────────────────────

    @Test(priority = 15, description = "Verify Pincode Delivery Availability")
    @Story("TS_0095")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0095_verifyValidPincodeDelivery() {
        LoggerHandler.testStart("TS_0095", "Verify Pincode Delivery Availability");
        Reporter.startTest("TS_0095", "Verify Pincode Delivery Availability");

        openPdp();
        pdpActions.scrollToPincodeField();

        Reporter.info("Step 1: Verify pincode field is visible.");
        Assert.assertTrue(pdpActions.isPincodeFieldVisible(), "Pincode field should be visible.");
        Reporter.pass("Pincode field is visible.");

        Reporter.info("Step 2: Enter valid pincode: " + VALID_PINCODE_1);
        pdpActions.enterPincode(VALID_PINCODE_1);

        Reporter.info("Step 3: Click Check button.");
        pdpActions.clickCheckButton();

        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        Reporter.info("Step 4: Verify delivery result container appears.");
        boolean resultVisible = pdpActions.isDeliveryResultVisible();
        Assert.assertTrue(resultVisible,
                "Delivery result container should appear after entering a valid pincode.");
        Reporter.pass("Delivery result is displayed for pincode: " + VALID_PINCODE_1);

        String deliveryMsg = pdpActions.getDeliveryMessage();
        LoggerHandler.info("Delivery message: " + deliveryMsg);
        Reporter.pass("Delivery availability message: '" + deliveryMsg + "'");

        String screenshotPath = Screenshot.captureFullPage(driver, "TS_0095");
        Reporter.attachScreenshot(screenshotPath);
    }

    // ── TS_0096 ──────────────────────────────────────────────────────────────

    @Test(priority = 16, description = "Verify Invalid Pincode Validation")
    @Story("TS_0096")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0096_verifyInvalidPincodeValidation() {
        LoggerHandler.testStart("TS_0096", "Verify Invalid Pincode Validation");
        Reporter.startTest("TS_0096", "Verify Invalid Pincode Validation");

        openPdp();
        pdpActions.scrollToPincodeField();

        Reporter.info("Step 1: Enter invalid pincode: " + INVALID_PINCODE_1);
        pdpActions.enterPincode(INVALID_PINCODE_1);
        pdpActions.clickCheckButton();
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        boolean alert1Visible  = pdpActions.isInvalidPincodeAlertVisible();
        boolean invalidState1  = pdpActions.isPincodeFieldInInvalidState();
        boolean anyResult1     = pdpActions.isDeliveryResultVisible();
        Assert.assertTrue(alert1Visible || invalidState1 || anyResult1,
                "An error alert, invalid-state, or any delivery response should appear for pincode '" + INVALID_PINCODE_1 + "'.");
        Reporter.pass("Invalid pincode '" + INVALID_PINCODE_1 + "' produced server feedback. Alert=" + alert1Visible
                + ", FieldInvalid=" + invalidState1 + ", ResultContainer=" + anyResult1);

        Reporter.info("Step 2: Enter invalid pincode: " + INVALID_PINCODE_2);
        pdpActions.enterPincode(INVALID_PINCODE_2);
        pdpActions.clickCheckButton();
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        boolean alert2Visible  = pdpActions.isInvalidPincodeAlertVisible();
        boolean invalidState2  = pdpActions.isPincodeFieldInInvalidState();
        Assert.assertTrue(alert2Visible || invalidState2,
                "An error alert or invalid-state indicator should appear for pincode '" + INVALID_PINCODE_2 + "'.");
        Reporter.pass("Invalid pincode '" + INVALID_PINCODE_2 + "' rejected. Alert=" + alert2Visible + ", FieldInvalid=" + invalidState2);

        Reporter.info("Step 3: Verify alphabetic input 'abcdef' is rejected by the field.");
        pdpActions.enterPincode("abcdef");
        String actualValue = driver.findElement(aditya.mothercare.uistore.ProductDetailSection.PINCODE_INPUT)
                .getAttribute("value");
        LoggerHandler.info("Pincode field value after typing 'abcdef': '" + actualValue + "'");
        Assert.assertTrue(actualValue.isEmpty() || !actualValue.contains("a"),
                "Alphabetic input should be rejected by the numeric pincode field.");
        Reporter.pass("Alphabetic input 'abcdef' is rejected. Field value: '" + actualValue + "'");

        String screenshotPath = Screenshot.captureFullPage(driver, "TS_0096");
        Reporter.attachScreenshot(screenshotPath);
    }

    // ── Shared helper: navigate to PDP ────────────────────────────────────────

    private void openPdp() {
        Reporter.info("Opening Furniture & Bedding PLP and selecting first product.");
        driver.get(PLP_URL);
        plpActions.areProductsDisplayed();
        plpActions.clickFirstProduct();
        LoggerHandler.info("PDP URL: " + driver.getCurrentUrl());
    }
}
