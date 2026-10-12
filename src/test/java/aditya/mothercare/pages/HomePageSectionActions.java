package aditya.mothercare.pages;

import aditya.mothercare.uistore.HomePageSection;
import aditya.mothercare.utils.LoggerHandler;
import aditya.mothercare.utils.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;

/**
 * Reusable actions for the Shop By Category section on the homepage.
 */
public class HomePageSectionActions {

    private final WebDriver driver;
    private final WebDriverHelper helper;

    public HomePageSectionActions(WebDriver driver) {
        this.driver = driver;
        this.helper = new WebDriverHelper(driver);
    }

    // ── Section visibility ───────────────────────────────────────────────────

    public boolean isShopByCategorySectionVisible() {
        try {
            WebElement section = helper.waitForVisible(HomePageSection.SHOP_BY_CATEGORY_SECTION);
            helper.scrollToElement(section);
            boolean visible = section.isDisplayed();
            LoggerHandler.info("Shop By Category section visible: " + visible);
            return visible;
        } catch (Exception e) {
            LoggerHandler.error("Shop By Category section not found: " + e.getMessage());
            return false;
        }
    }

    public boolean isHeadingVisible() {
        try {
            WebElement h3 = helper.waitForVisible(HomePageSection.SHOP_BY_CATEGORY_HEADING);
            boolean visible = h3.isDisplayed();
            LoggerHandler.info("Category heading text: '" + h3.getText() + "', visible: " + visible);
            return visible;
        } catch (Exception e) {
            LoggerHandler.error("Category heading not found: " + e.getMessage());
            return false;
        }
    }

    public String getHeadingText() {
        return helper.waitForVisible(HomePageSection.SHOP_BY_CATEGORY_HEADING).getText().trim();
    }

    public boolean isCarouselVisible() {
        return helper.isDisplayed(HomePageSection.CATEGORY_CAROUSEL);
    }

    // ── Tile and image checks ────────────────────────────────────────────────

    public List<WebElement> getAllTiles() {
        return driver.findElements(HomePageSection.ALL_CATEGORY_TILES);
    }

    public List<WebElement> getAllImages() {
        return driver.findElements(HomePageSection.ALL_CATEGORY_IMAGES);
    }

    public List<WebElement> getAllLinks() {
        return driver.findElements(HomePageSection.ALL_CATEGORY_LINKS);
    }

    public boolean isTileImageVisible(By imageLocator) {
        try {
            WebElement img = helper.waitForVisible(imageLocator);
            boolean loaded = helper.isImageLoaded(img);
            LoggerHandler.info("Image visible=" + img.isDisplayed() + " loaded=" + loaded + " for: " + imageLocator);
            return img.isDisplayed() && loaded;
        } catch (Exception e) {
            LoggerHandler.error("Image not found: " + imageLocator + " - " + e.getMessage());
            return false;
        }
    }

    // ── Tile alignment checks ────────────────────────────────────────────────

    /**
     * Returns the Y-coordinate of each tile.
     * Used in TS_0084 to verify horizontal alignment (same row → same Y).
     */
    public int[] getTileYPositions() {
        List<WebElement> tiles = getAllTiles();
        int[] yPositions = new int[tiles.size()];
        for (int i = 0; i < tiles.size(); i++) {
            // Re-fetch each tile by index to avoid stale reference from carousel re-render
            try {
                yPositions[i] = driver.findElements(HomePageSection.ALL_CATEGORY_TILES).get(i).getLocation().getY();
            } catch (Exception e) {
                yPositions[i] = tiles.get(i).getLocation().getY();
            }
        }
        return yPositions;
    }

    public int[] getTileWidths() {
        List<WebElement> tiles = getAllTiles();
        int[] widths = new int[tiles.size()];
        for (int i = 0; i < tiles.size(); i++) {
            // Re-fetch each tile by index to avoid stale reference from carousel re-render
            try {
                widths[i] = driver.findElements(HomePageSection.ALL_CATEGORY_TILES).get(i).getSize().getWidth();
            } catch (Exception e) {
                widths[i] = tiles.get(i).getSize().getWidth();
            }
        }
        return widths;
    }

    // ── Category clicks ──────────────────────────────────────────────────────

    /**
     * Snapshots open handles, JS-clicks the category link, and returns the snapshot.
     * Pass the returned set to wdh.switchToNewTab(handles) so the test always lands
     * on the tab this specific click opened.
     */
    public Set<String> clickCategory(By linkLocator) {
        scrollToCategory(linkLocator);
        Set<String> handlesBefore = helper.getWindowHandles();
        WebElement link = helper.waitForVisible(linkLocator);
        helper.jsClick(link);
        LoggerHandler.info("JS-clicked category: " + linkLocator);
        return handlesBefore;
    }

    /**
     * Scrolls a category tile into view; needed for off-screen tiles (e.g., Baby Care).
     * Re-finds the element fresh each call to avoid stale references.
     */
    public void scrollToCategory(By locator) {
        try {
            // Wait for it to be present, then scroll
            WebElement el = helper.waitForVisible(locator);
            helper.scrollToElement(el);
        } catch (Exception e) {
            LoggerHandler.warn("Could not scroll to category: " + locator + " — " + e.getMessage());
        }
    }

    // ── Convenience per-category methods ────────────────────────────────────

    public Set<String> clickSale()               { return clickCategory(HomePageSection.SALE_LINK); }
    public Set<String> clickBabyClothing()       { return clickCategory(HomePageSection.BABY_CLOTHING_LINK); }
    public Set<String> clickMomsMaternity()      { return clickCategory(HomePageSection.MOMS_MATERNITY_LINK); }
    public Set<String> clickFurnitureBedding()   { return clickCategory(HomePageSection.FURNITURE_BEDDING_LINK); }
    public Set<String> clickFootwearAccessories(){ return clickCategory(HomePageSection.FOOTWEAR_ACCESSORIES_LINK); }
    public Set<String> clickBabyGear()           { return clickCategory(HomePageSection.BABY_GEAR_LINK); }
    public Set<String> clickBabyCare()           { return clickCategory(HomePageSection.BABY_CARE_LINK); }

    public boolean isSaleImageVisible()               { return isTileImageVisible(HomePageSection.SALE_IMAGE); }
    public boolean isBabyClothingImageVisible()       { return isTileImageVisible(HomePageSection.BABY_CLOTHING_IMAGE); }
    public boolean isMomsMaternityImageVisible()      { return isTileImageVisible(HomePageSection.MOMS_MATERNITY_IMAGE); }
    public boolean isFurnitureBeddingImageVisible()   { return isTileImageVisible(HomePageSection.FURNITURE_BEDDING_IMAGE); }
    public boolean isFootwearAccessoriesImageVisible(){ return isTileImageVisible(HomePageSection.FOOTWEAR_ACCESSORIES_IMAGE); }
    public boolean isBabyGearImageVisible()           { return isTileImageVisible(HomePageSection.BABY_GEAR_IMAGE); }
    public boolean isBabyCareImageVisible()           { return isTileImageVisible(HomePageSection.BABY_CARE_IMAGE); }
}
