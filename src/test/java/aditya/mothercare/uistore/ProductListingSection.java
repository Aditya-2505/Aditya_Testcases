package aditya.mothercare.uistore;

import org.openqa.selenium.By;

/**
 * Locators for the Product Listing Page (PLP).
 * Covers TS_0086 – TS_0091.
 * All locators are supplied and approved; do not modify without approval.
 */
public class ProductListingSection {

    // ── Page container ───────────────────────────────────────────────────────

    public static final By LISTING_CONTAINER =
            By.cssSelector("main");

    // ── Category heading ─────────────────────────────────────────────────────

    public static final By CATEGORY_HEADING =
            By.cssSelector("main div:has(> #dy__plp_collection_title) > h2");

    // ── Product grid ─────────────────────────────────────────────────────────

    public static final By PRODUCT_GRID =
            By.cssSelector("main div:has(> div > a[href^='/product/'])");

    public static final By PRODUCT_CARDS =
            By.cssSelector("main div:has(> a[href^='/product/'])");

    public static final By PRODUCT_LINKS =
            By.cssSelector("main a[href^='/product/']");

    // ── Product images ───────────────────────────────────────────────────────

    public static final By PRODUCT_IMAGES =
            By.cssSelector("main a[href^='/product/'] img.plp_product_img_dy:not([alt=''])");

    // ── Product names ────────────────────────────────────────────────────────

    public static final By PRODUCT_NAMES =
            By.cssSelector("main a[href^='/product/'] h2");

    // ── Product prices ───────────────────────────────────────────────────────

    public static final By PRODUCT_PRICES =
            By.cssSelector("main a[href^='/product/'] div:has(> h2) + div");

    // ── Breadcrumb ───────────────────────────────────────────────────────────

    public static final By BREADCRUMB =
            By.cssSelector("nav[aria-label='Breadcrumb']");

    public static final By BREADCRUMB_ITEMS =
            By.cssSelector("nav[aria-label='Breadcrumb'] li");

    public static final By BREADCRUMB_HOME =
            By.cssSelector("nav[aria-label='Breadcrumb'] a[aria-label='Home']");

    public static final By BREADCRUMB_CURRENT =
            By.cssSelector("nav[aria-label='Breadcrumb'] [aria-current='page']");
}
