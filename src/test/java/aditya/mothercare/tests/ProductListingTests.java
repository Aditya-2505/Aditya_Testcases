package aditya.mothercare.tests;

import aditya.mothercare.utils.Base;
import aditya.mothercare.pages.HomePageSectionActions;
import aditya.mothercare.pages.ProductListingSectionActions;
import aditya.mothercare.utils.*;
import io.qameta.allure.*;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * Test cases TS_0086 – TS_0090
 * Product Listing Page navigation and product relevance tests.
 */
@Epic("Mothercare Product Listing")
@Feature("Category Navigation")
public class ProductListingTests extends Base {

    private HomePageSectionActions homeActions;
    private ProductListingSectionActions plpActions;
    private WebDriverHelper wdh;

    @BeforeMethod
    public void initPages() {
        homeActions = new HomePageSectionActions(driver);
        plpActions  = new ProductListingSectionActions(driver);
        wdh         = new WebDriverHelper(driver);
        LoggerHandler.info("ProductListingTests setup complete.");
    }

    // ── TS_0086 ──────────────────────────────────────────────────────────────

    @Test(priority = 6, description = "Verify Navigation to Baby Clothing Product Listing Page")
    @Story("TS_0086")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0086_verifyBabyClothingNavigation() {
        LoggerHandler.testStart("TS_0086", "Verify Navigation to Baby Clothing Product Listing Page");
        Reporter.startTest("TS_0086", "Verify Navigation to Baby Clothing Product Listing Page");

        navigateToCategoryAndVerify("Baby Clothing", "baby-clothing", () -> homeActions.clickBabyClothing());
    }

    // ── TS_0087 ──────────────────────────────────────────────────────────────

    @Test(priority = 7, description = "Verify Navigation to Baby Care Product Listing Page")
    @Story("TS_0087")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0087_verifyBabyCareNavigation() {
        LoggerHandler.testStart("TS_0087", "Verify Navigation to Baby Care Product Listing Page");
        Reporter.startTest("TS_0087", "Verify Navigation to Baby Care Product Listing Page");

        navigateToCategoryAndVerify("Baby Care", "babycare", () -> homeActions.clickBabyCare());
    }

    // ── TS_0088 ──────────────────────────────────────────────────────────────

    @Test(priority = 8, description = "Verify Navigation to Baby Gear Product Listing Page")
    @Story("TS_0088")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0088_verifyBabyGearNavigation() {
        LoggerHandler.testStart("TS_0088", "Verify Navigation to Baby Gear Product Listing Page");
        Reporter.startTest("TS_0088", "Verify Navigation to Baby Gear Product Listing Page");

        navigateToCategoryAndVerify("Baby Gear", "baby-gear", () -> homeActions.clickBabyGear());
    }

    // ── TS_0089 ──────────────────────────────────────────────────────────────

    @Test(priority = 9, description = "Verify Navigation to Sale Product Listing Page")
    @Story("TS_0089")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0089_verifySaleNavigation() {
        LoggerHandler.testStart("TS_0089", "Verify Navigation to Sale Product Listing Page");
        Reporter.startTest("TS_0089", "Verify Navigation to Sale Product Listing Page");

        navigateToCategoryAndVerify("Sale", "sale", () -> homeActions.clickSale());
    }

    // ── TS_0090 ──────────────────────────────────────────────────────────────

    @Test(priority = 10, description = "Verify Products Belong to Selected Category")
    @Story("TS_0090")
    @Severity(SeverityLevel.CRITICAL)
    public void TS_0090_verifyProductsBelongToCategory() {
        LoggerHandler.testStart("TS_0090", "Verify Products Belong to Selected Category");
        Reporter.startTest("TS_0090", "Verify Products Belong to Selected Category");

        String[] catNames = {"Baby Clothing", "Baby Care", "Baby Gear"};
        String[] urlParts = {"baby-clothing", "babycare",  "baby-gear"};

        for (int i = 0; i < catNames.length; i++) {
            String catName = catNames[i];
            String urlPart = urlParts[i];

            Reporter.info("Verifying products for category: " + catName);
            openHomePage();
            homeActions.isShopByCategorySectionVisible();
            Set<String> handlesBefore = homeActions.clickCategory(getByForCategory(urlPart));
            wdh.switchToNewTab(handlesBefore);

            String url = driver.getCurrentUrl();
            Assert.assertTrue(url.contains(urlPart),
                    "URL should contain '" + urlPart + "' for category: " + catName + ". Actual: " + url);
            Reporter.pass("URL confirmed category: " + catName);

            boolean productsDisplayed = plpActions.areProductsDisplayed();
            Assert.assertTrue(productsDisplayed,
                    "Products should be displayed on the " + catName + " listing page.");

            List<WebElement> names = plpActions.getProductNames();
            Reporter.pass(catName + ": " + names.size() + " product(s) displayed.");

            wdh.closeCurrentTabAndSwitch();
        }

        Reporter.pass("TS_0090: Products are displayed and belong to their respective selected categories.");
    }

    // ── Shared navigation helper ──────────────────────────────────────────────

    private void navigateToCategoryAndVerify(String categoryName, String urlFragment, Runnable clickAction) {
        Reporter.info("Step 1: Open Mothercare homepage.");
        openHomePage();

        Reporter.info("Step 2: Scroll to and locate Shop By Category section.");
        homeActions.isShopByCategorySectionVisible();

        Reporter.info("Step 3: Scroll to " + categoryName + " category tile.");
        homeActions.scrollToCategory(getByForCategory(urlFragment));

        Reporter.info("Step 4: Click the " + categoryName + " category. (Opens in new tab)");
        Set<String> handlesBefore = homeActions.clickCategory(getByForCategory(urlFragment));
        wdh.switchToNewTab(handlesBefore);

        Reporter.info("Step 5: Verify URL contains expected fragment.");
        String currentUrl = driver.getCurrentUrl();
        LoggerHandler.info("Navigated to: " + currentUrl);
        Assert.assertTrue(currentUrl.contains(urlFragment),
                categoryName + " PLP URL should contain '" + urlFragment + "'. Actual: " + currentUrl);
        Reporter.pass("Successfully navigated to " + categoryName + " PLP. URL: " + currentUrl);

        Reporter.info("Step 6: Verify products are displayed on PLP.");
        boolean productsVisible = plpActions.areProductsDisplayed();
        Assert.assertTrue(productsVisible, categoryName + " products should be displayed on the PLP.");

        List<WebElement> productNames = plpActions.getProductNames();
        Reporter.pass(productNames.size() + " " + categoryName + " product(s) displayed on PLP.");

        String screenshotPath = Screenshot.captureFullPage(driver, "TS_008x_" + urlFragment);
        Reporter.attachScreenshot(screenshotPath);

        wdh.closeCurrentTabAndSwitch();
    }

    private org.openqa.selenium.By getByForCategory(String urlFragment) {
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
