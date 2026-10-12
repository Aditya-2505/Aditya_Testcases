package aditya.mothercare.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Helper methods for common Selenium operations.
 * Every interactive action highlights the target element first (yellow border).
 */
public class WebDriverHelper {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WebDriverHelper(WebDriver driver) {
        this.driver = driver;
        int timeout = Integer.parseInt(Base.getConfig("explicit.wait", "20"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    // ── Highlight ────────────────────────────────────────────────────────────

    public void highlightElement(By locator) {
        try {
            WebElement el = driver.findElement(locator);
            highlightElement(el);
        } catch (Exception ignored) {}
    }

    public void highlightElement(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.border='3px solid yellow';" +
                    "arguments[0].style.backgroundColor='#fff59d';", el);
        } catch (Exception ignored) {}
    }

    // ── Wait helpers ─────────────────────────────────────────────────────────

    public WebElement waitForVisible(By locator) {
        LoggerHandler.debug("Waiting for visible: " + locator);
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        highlightElement(el);
        return el;
    }

    public WebElement waitForClickable(By locator) {
        LoggerHandler.debug("Waiting for clickable: " + locator);
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        highlightElement(el);
        return el;
    }

    public List<WebElement> waitForAllVisible(By locator) {
        LoggerHandler.debug("Waiting for all visible: " + locator);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public boolean waitForUrlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    // ── Click helpers ─────────────────────────────────────────────────────────

    public void click(By locator) {
        WebElement el = waitForClickable(locator);
        highlightElement(el);
        LoggerHandler.debug("Clicking: " + locator);
        el.click();
    }

    public void jsClick(By locator) {
        WebElement el = waitForVisible(locator);
        highlightElement(el);
        LoggerHandler.debug("JS-clicking: " + locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void jsClick(WebElement el) {
        highlightElement(el);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    // ── Input helpers ─────────────────────────────────────────────────────────

    public void clearAndType(By locator, String text) {
        WebElement el = waitForVisible(locator);
        highlightElement(el);
        el.clear();
        el.sendKeys(text);
        LoggerHandler.debug("Typed '" + text + "' into: " + locator);
    }

    public String getText(By locator) {
        WebElement el = waitForVisible(locator);
        highlightElement(el);
        return el.getText().trim();
    }

    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    // ── Scroll helpers ────────────────────────────────────────────────────────

    public void scrollToElement(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        highlightElement(el);
    }

    public void scrollToElement(By locator) {
        WebElement el = waitForVisible(locator);
        scrollToElement(el);
    }

    public void scrollBy(int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + pixels + ");");
    }

    // ── Tab helpers ──────────────────────────────────────────────────────────

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * Waits for a handle NOT in handlesBefore, then switches to it.
     * Always lands on the tab the click just opened.
     */
    public void switchToNewTab(Set<String> handlesBefore) {
        wait.until(d -> d.getWindowHandles().size() > handlesBefore.size());
        for (String h : driver.getWindowHandles()) {
            if (!handlesBefore.contains(h)) {
                driver.switchTo().window(h);
                wait.until(d -> !((JavascriptExecutor) d)
                        .executeScript("return document.readyState").equals("loading"));
                LoggerHandler.debug("Switched to new tab: " + driver.getCurrentUrl());
                return;
            }
        }
    }

    public void switchToNewTab() {
        switchToNewTab(Set.of(driver.getWindowHandle()));
    }

    public void closeCurrentTabAndSwitch() {
        driver.close();
        String remaining = driver.getWindowHandles().iterator().next();
        driver.switchTo().window(remaining);
        LoggerHandler.debug("Closed tab, switched back to: " + driver.getCurrentUrl());
    }

    // ── Image check ──────────────────────────────────────────────────────────

    public boolean isImageLoaded(WebElement img) {
        Object result = ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].complete && arguments[0].naturalWidth > 0;", img);
        return Boolean.TRUE.equals(result);
    }

    // ── CSS property ─────────────────────────────────────────────────────────

    public String getCssValue(WebElement el, String property) {
        return el.getCssValue(property);
    }

    public WebDriverWait getWait() {
        return wait;
    }
}
