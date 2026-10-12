package aditya.mothercare.uistore;

import org.openqa.selenium.By;

/**
 * Locators for the Shop By Category section on the Mothercare homepage.
 * Covers TS_0081 – TS_0090.
 * All locators are supplied and approved; do not modify without approval.
 */
public class HomePageSection {

    // ── Section and heading ──────────────────────────────────────────────────

    public static final By SHOP_BY_CATEGORY_SECTION =
            By.cssSelector("div.fdk-theme-wrapper:has(h3)");

    public static final By SHOP_BY_CATEGORY_HEADING =
            By.cssSelector("div.fdk-theme-wrapper h3");

    // ── Carousel ─────────────────────────────────────────────────────────────

    public static final By CATEGORY_CAROUSEL =
            By.cssSelector("div.fdk-theme-wrapper:has(h3) div[aria-roledescription='carousel']");

    // ── All tiles, images, links ─────────────────────────────────────────────

    public static final By ALL_CATEGORY_TILES =
            By.cssSelector("div.fdk-theme-wrapper:has(h3) div[aria-roledescription='slide']");

    public static final By ALL_CATEGORY_IMAGES =
            By.cssSelector("div.fdk-theme-wrapper:has(h3) div[aria-roledescription='slide'] img");

    public static final By ALL_CATEGORY_LINKS =
            By.cssSelector("div.fdk-theme-wrapper:has(h3) div[aria-roledescription='slide'] a");

    // ── Sale ─────────────────────────────────────────────────────────────────

    public static final By SALE_TILE =
            By.cssSelector("div[aria-roledescription='slide']:has(a[href='https://mothercare.in/collection/sale'])");

    public static final By SALE_LINK =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/sale']");

    public static final By SALE_IMAGE =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/sale'] img");

    // ── Baby Clothing ────────────────────────────────────────────────────────

    public static final By BABY_CLOTHING_TILE =
            By.cssSelector("div[aria-roledescription='slide']:has(a[href='https://mothercare.in/collection/baby-clothing'])");

    public static final By BABY_CLOTHING_LINK =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/baby-clothing']");

    public static final By BABY_CLOTHING_IMAGE =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/baby-clothing'] img");

    // ── Moms & Maternity ─────────────────────────────────────────────────────

    public static final By MOMS_MATERNITY_TILE =
            By.cssSelector("div[aria-roledescription='slide']:has(a[href='https://mothercare.in/collection/moms--maternity'])");

    public static final By MOMS_MATERNITY_LINK =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/moms--maternity']");

    public static final By MOMS_MATERNITY_IMAGE =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/moms--maternity'] img");

    // ── Furniture & Bedding ──────────────────────────────────────────────────

    public static final By FURNITURE_BEDDING_TILE =
            By.cssSelector("div[aria-roledescription='slide']:has(a[href='https://mothercare.in/collection/furniture--bedding'])");

    public static final By FURNITURE_BEDDING_LINK =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/furniture--bedding']");

    public static final By FURNITURE_BEDDING_IMAGE =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/furniture--bedding'] img");

    // ── Footwear & Accessories ───────────────────────────────────────────────

    public static final By FOOTWEAR_ACCESSORIES_TILE =
            By.cssSelector("div[aria-roledescription='slide']:has(a[href='https://mothercare.in/collection/footwear--accessories'])");

    public static final By FOOTWEAR_ACCESSORIES_LINK =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/footwear--accessories']");

    public static final By FOOTWEAR_ACCESSORIES_IMAGE =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/footwear--accessories'] img");

    // ── Baby Gear ────────────────────────────────────────────────────────────

    public static final By BABY_GEAR_TILE =
            By.cssSelector("div[aria-roledescription='slide']:has(a[href='https://mothercare.in/collection/baby-gear'])");

    public static final By BABY_GEAR_LINK =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/baby-gear']");

    public static final By BABY_GEAR_IMAGE =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/baby-gear'] img");

    // ── Baby Care (may be off-screen at 1280 px; JS-scroll before clicking) ──

    public static final By BABY_CARE_TILE =
            By.cssSelector("div[aria-roledescription='slide']:has(a[href='https://mothercare.in/collection/babycare'])");

    public static final By BABY_CARE_LINK =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/babycare']");

    public static final By BABY_CARE_IMAGE =
            By.cssSelector("div[aria-roledescription='slide'] a[href='https://mothercare.in/collection/babycare'] img");
}
