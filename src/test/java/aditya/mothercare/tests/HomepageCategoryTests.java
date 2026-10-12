package aditya.mothercare.tests;

import aditya.mothercare.utils.Base;
import aditya.mothercare.pages.HomePageSectionActions;
import aditya.mothercare.utils.*;
import io.qameta.allure.*;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

/**
 * Test cases TS_0081 – TS_0085
 * Homepage Shop By Category section tests.
 */
@Epic("Mothercare Homepage")
@Feature("Shop By Category")
public class HomepageCategoryTests extends Base {

    private HomePageSectionActions homeActions;

    @BeforeMethod
    public void initPages() {
        homeActions = new HomePageSectionActions(driver);
        LoggerHandler.info("HomepageCategoryTests setup complete.");
    }

    // ── TS_0081 ──────────────────────────────────────────────────────────────

    @Test(priority = 1, description = "Verify Visibility of Shop By Category Section")
    @Story("TS_0081")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0081_verifyShopByCategorySectionVisibility() {
        LoggerHandler.testStart("TS_0081", "Verify Visibility of Shop By Category Section");
        Reporter.startTest("TS_0081", "Verify Visibility of Shop By Category Section");

        Reporter.info("Step 1: Open Mothercare homepage.");
        openHomePage();

        Reporter.info("Step 2: Scroll to and locate Shop By Category section.");
        boolean sectionVisible = homeActions.isShopByCategorySectionVisible();
        Assert.assertTrue(sectionVisible, "Shop By Category section should be visible on homepage.");
        Reporter.pass("Shop By Category section is visible.");

        Reporter.info("Step 3: Verify section heading is visible.");
        boolean headingVisible = homeActions.isHeadingVisible();
        Assert.assertTrue(headingVisible, "Shop By Category heading should be visible.");
        Reporter.pass("Section heading is visible: '" + homeActions.getHeadingText() + "'");

        Reporter.info("Step 4: Verify carousel/category section is accessible.");
        boolean carouselVisible = homeActions.isCarouselVisible();
        Assert.assertTrue(carouselVisible, "Category carousel should be accessible.");
        Reporter.pass("Category carousel is accessible.");
    }

    // ── TS_0082 ──────────────────────────────────────────────────────────────

    @Test(priority = 2, description = "Verify Category Images are Displayed Correctly")
    @Story("TS_0082")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0082_verifyCategoryImagesDisplayed() {
        LoggerHandler.testStart("TS_0082", "Verify Category Images are Displayed Correctly");
        Reporter.startTest("TS_0082", "Verify Category Images are Displayed Correctly");

        openHomePage();
        homeActions.isShopByCategorySectionVisible();

        Reporter.info("Verifying Sale image.");
        Assert.assertTrue(homeActions.isSaleImageVisible(), "Sale image should be visible and loaded.");
        Reporter.pass("Sale image is displayed correctly.");

        Reporter.info("Verifying Baby Clothing image.");
        Assert.assertTrue(homeActions.isBabyClothingImageVisible(), "Baby Clothing image should be visible.");
        Reporter.pass("Baby Clothing image is displayed correctly.");

        Reporter.info("Verifying Moms & Maternity image.");
        Assert.assertTrue(homeActions.isMomsMaternityImageVisible(), "Moms & Maternity image should be visible.");
        Reporter.pass("Moms & Maternity image is displayed correctly.");

        Reporter.info("Verifying Furniture & Bedding image.");
        Assert.assertTrue(homeActions.isFurnitureBeddingImageVisible(), "Furniture & Bedding image should be visible.");
        Reporter.pass("Furniture & Bedding image is displayed correctly.");

        Reporter.info("Verifying Footwear & Accessories image.");
        Assert.assertTrue(homeActions.isFootwearAccessoriesImageVisible(), "Footwear & Accessories image should be visible.");
        Reporter.pass("Footwear & Accessories image is displayed correctly.");

        Reporter.info("Verifying Baby Gear image.");
        Assert.assertTrue(homeActions.isBabyGearImageVisible(), "Baby Gear image should be visible.");
        Reporter.pass("Baby Gear image is displayed correctly.");

        Reporter.info("Verifying Baby Care image (may require scroll).");
        homeActions.scrollToCategory(aditya.mothercare.uistore.HomePageSection.BABY_CARE_TILE);
        Assert.assertTrue(homeActions.isBabyCareImageVisible(), "Baby Care image should be visible.");
        Reporter.pass("Baby Care image is displayed correctly.");

        Reporter.info("Checking all tiles for broken images.");
        List<WebElement> allImages = homeActions.getAllImages();
        int brokenCount = 0;
        for (WebElement img : allImages) {
            if (!new WebDriverHelper(driver).isImageLoaded(img)) brokenCount++;
        }
        Assert.assertEquals(brokenCount, 0, brokenCount + " broken image(s) found.");
        Reporter.pass("No broken images found. All " + allImages.size() + " images loaded successfully.");
    }

    // ── TS_0083 ──────────────────────────────────────────────────────────────

    @Test(priority = 3, description = "Verify Category Names are Displayed Correctly")
    @Story("TS_0083")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0083_verifyCategoryNamesDisplayed() {
        LoggerHandler.testStart("TS_0083", "Verify Category Names are Displayed Correctly");
        Reporter.startTest("TS_0083", "Verify Category Names are Displayed Correctly");

        openHomePage();
        homeActions.isShopByCategorySectionVisible();

        String[][] categories = {
            {"Sale",                   "sale"},
            {"Baby Clothing",          "baby-clothing"},
            {"Moms & Maternity",       "moms--maternity"},
            {"Furniture & Bedding",    "furniture--bedding"},
            {"Footwear & Accessories", "footwear--accessories"},
            {"Baby Gear",              "baby-gear"},
            {"Baby Care",              "babycare"}
        };

        for (String[] cat : categories) {
            String name = cat[0];
            String urlPart = cat[1];
            Reporter.info("Verifying category: " + name);

            List<WebElement> links = homeActions.getAllLinks();
            boolean found = links.stream()
                    .anyMatch(a -> a.getAttribute("href") != null && a.getAttribute("href").contains(urlPart));

            Assert.assertTrue(found,
                    "Category '" + name + "' link (containing '" + urlPart + "') not found in carousel.");
            Reporter.pass("Category '" + name + "' is represented in the carousel.");
        }

        Reporter.pass("All 7 category names are verified as present in the Shop By Category section.");
    }

    // ── TS_0084 ──────────────────────────────────────────────────────────────

    @Test(priority = 4, description = "Verify Alignment of Category Tiles")
    @Story("TS_0084")
    @Severity(SeverityLevel.NORMAL)
    public void TS_0084_verifyCategoryTileAlignment() {
        LoggerHandler.testStart("TS_0084", "Verify Alignment of Category Tiles");
        Reporter.startTest("TS_0084", "Verify Alignment of Category Tiles");

        openHomePage();
        homeActions.isShopByCategorySectionVisible();

        List<WebElement> tiles = homeActions.getAllTiles();
        Assert.assertFalse(tiles.isEmpty(), "Category tiles should be present.");
        Reporter.info("Total tiles found: " + tiles.size());

        int[] widths = homeActions.getTileWidths();
        int referenceWidth = widths[0];
        boolean widthConsistent = true;
        for (int w : widths) {
            if (Math.abs(w - referenceWidth) > 5) {
                widthConsistent = false;
                Reporter.warn("Tile width inconsistency detected: " + w + " vs reference " + referenceWidth);
            }
        }
        Assert.assertTrue(widthConsistent, "All visible category tiles should have consistent widths.");
        Reporter.pass("Category tile widths are consistent: ~" + referenceWidth + "px.");

        int[] yPositions = homeActions.getTileYPositions();
        int referenceY = yPositions[0];
        boolean horizontallyAligned = true;
        for (int y : yPositions) {
            if (Math.abs(y - referenceY) > 20) {
                horizontallyAligned = false;
                Reporter.warn("Tile Y-position deviation: " + y + " vs reference " + referenceY);
                break;
            }
        }
        Assert.assertTrue(horizontallyAligned, "Category tiles should be horizontally aligned in the carousel.");
        Reporter.pass("Category tiles are horizontally aligned. Y reference: " + referenceY + "px.");

        Reporter.pass("TS_0084: Tile alignment verified — widths uniform, tiles on same row.");
    }

    // ── TS_0085 ──────────────────────────────────────────────────────────────

    @Test(priority = 5, description = "Verify All Category Tiles are Clickable")
    @Story("TS_0085")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0085_verifyAllCategoryTilesClickable() {
        LoggerHandler.testStart("TS_0085", "Verify All Category Tiles are Clickable");
        Reporter.startTest("TS_0085", "Verify All Category Tiles are Clickable");

        String[][] categories = {
            {"Sale",                   "sale"},
            {"Baby Clothing",          "baby-clothing"},
            {"Moms & Maternity",       "moms--maternity"},
            {"Furniture & Bedding",    "furniture--bedding"},
            {"Footwear & Accessories", "footwear--accessories"},
            {"Baby Gear",              "baby-gear"},
            {"Baby Care",              "babycare"}
        };

        WebDriverHelper wdh = new WebDriverHelper(driver);

        for (String[] cat : categories) {
            String catName = cat[0];
            String urlFragment = cat[1];

            Reporter.info("Testing click on: " + catName);

            openHomePage();
            homeActions.isShopByCategorySectionVisible();

            homeActions.scrollToCategory(getBy(urlFragment));
            java.util.Set<String> handlesBefore = homeActions.clickCategory(getBy(urlFragment));
            wdh.switchToNewTab(handlesBefore);
            String newUrl = driver.getCurrentUrl();
            LoggerHandler.info("Navigated to: " + newUrl);

            Assert.assertTrue(newUrl.contains(urlFragment),
                    catName + " tile click should navigate to URL containing '" + urlFragment + "'. Actual: " + newUrl);
            Reporter.pass(catName + " category tile is clickable. Navigated to: " + newUrl);

            wdh.closeCurrentTabAndSwitch();
        }

        Reporter.pass("All 7 category tiles are clickable and navigate correctly.");
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private org.openqa.selenium.By getBy(String urlFragment) {
        return switch (urlFragment) {
            case "sale"                  -> aditya.mothercare.uistore.HomePageSection.SALE_LINK;
            case "baby-clothing"         -> aditya.mothercare.uistore.HomePageSection.BABY_CLOTHING_LINK;
            case "moms--maternity"       -> aditya.mothercare.uistore.HomePageSection.MOMS_MATERNITY_LINK;
            case "furniture--bedding"    -> aditya.mothercare.uistore.HomePageSection.FURNITURE_BEDDING_LINK;
            case "footwear--accessories" -> aditya.mothercare.uistore.HomePageSection.FOOTWEAR_ACCESSORIES_LINK;
            case "baby-gear"             -> aditya.mothercare.uistore.HomePageSection.BABY_GEAR_LINK;
            default                      -> aditya.mothercare.uistore.HomePageSection.BABY_CARE_LINK;
        };
    }
}
