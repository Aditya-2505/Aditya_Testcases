package aditya.mothercare.uistore;

import org.openqa.selenium.By;

/**
 * Locators for the Product Detail Page (PDP) and Pincode/Delivery section.
 * Covers TS_0091 – TS_0096.
 * All locators are supplied and approved; do not modify without approval.
 */
public class ProductDetailSection {

    // ── Product information ──────────────────────────────────────────────────

    public static final By PRODUCT_NAME =
            By.cssSelector("div.product--description-pdp h1");

    public static final By PRODUCT_IMAGE =
            By.cssSelector("div.product--image-slider div.glide__track img");

    public static final By PRODUCT_PRICE =
            By.cssSelector("div.product--description-pdp h4");

    // ── Quantity controls ────────────────────────────────────────────────────

    public static final By QUANTITY_SELECTOR =
            By.cssSelector("div.product--description-pdp div:has(> button + span + button)");

    public static final By QUANTITY_DISPLAY =
            By.cssSelector("div.product--description-pdp button + span:has(+ button)");

    public static final By DECREASE_BUTTON =
            By.cssSelector("div.product--description-pdp button:has(+ span + button)");

    public static final By INCREASE_BUTTON =
            By.cssSelector("div.product--description-pdp button + span + button");

    // ── Cart and wishlist ────────────────────────────────────────────────────

    public static final By ADD_TO_CART_BUTTON =
            By.cssSelector("div.product--description-pdp button[class*='fy-button__primary']");

    public static final By ADD_TO_WISHLIST_BUTTON =
            By.cssSelector("div.product--description-pdp button[aria-pressed]");

    // ── Pincode / Delivery ───────────────────────────────────────────────────

    public static final By PINCODE_INPUT =
            By.cssSelector("input[inputmode='numeric'][maxlength='6']");

    public static final By CHECK_BUTTON =
            By.cssSelector("input[inputmode='numeric'][maxlength='6'] + button");

    public static final By DELIVERY_RESULT_CONTAINER =
            By.cssSelector("div:has(> button[aria-label='Close delivery message'])");

    public static final By DELIVERY_AVAILABILITY_MESSAGE =
            By.cssSelector("button[aria-label='Close delivery message'] + span");

    /** Appears for both 123 and 000000; the field does not accept alphabetic input. */
    public static final By INVALID_PINCODE_ALERT =
            By.cssSelector("div[role='alert']:has(> button[aria-label='Close delivery message'])");

    /** Only present when the field enters the invalid/error state. */
    public static final By PINCODE_INPUT_INVALID =
            By.cssSelector("input[inputmode='numeric'][maxlength='6'][aria-invalid='true']");
}
