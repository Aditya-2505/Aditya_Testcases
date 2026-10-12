package aditya.mothercare.utils;

import aditya.mothercare.utils.Base;
import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Three screenshot types:
 *   A) Full page / browser window
 *   B) Specific element crop
 *   C) Highlight element, capture, restore
 */
public class Screenshot {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String DIR = Base.getConfig("screenshot.dir", "screenshots");

    private Screenshot() {}

    // ── A: Full-page screenshot ──────────────────────────────────────────────

    public static String captureFullPage(WebDriver driver, String testCaseId) {
        return capture(driver, testCaseId, "FULL", null);
    }

    // ── B: Element screenshot ────────────────────────────────────────────────

    public static String captureElement(WebDriver driver, WebElement element, String testCaseId) {
        return capture(driver, testCaseId, "ELEMENT", element);
    }

    // ── C: Highlight screenshot ──────────────────────────────────────────────

    public static String captureHighlight(WebDriver driver, WebElement element, String testCaseId) {
        String original = element.getAttribute("style");
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid red'; arguments[0].style.backgroundColor='yellow';", element);
            return capture(driver, testCaseId, "HIGHLIGHT", null);
        } finally {
            // Always restore original style
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].setAttribute('style', arguments[1]);", element, original == null ? "" : original);
            } catch (Exception ignored) {}
        }
    }

    // ── Core capture logic ───────────────────────────────────────────────────

    private static String capture(WebDriver driver, String testCaseId, String type, WebElement element) {
        try {
            new File(DIR).mkdirs();
            String timestamp = LocalDateTime.now().format(FMT);
            String fileName = DIR + "/" + testCaseId + "_" + type + "_" + timestamp + ".png";

            TakesScreenshot ts = (TakesScreenshot) (element != null ? element : driver);
            File src = ts.getScreenshotAs(OutputType.FILE);
            FileHandler.copy(src, new File(fileName));

            LoggerHandler.info("Screenshot saved: " + fileName);
            return fileName;
        } catch (Exception e) {
            LoggerHandler.error("Screenshot failed [" + type + "]: " + e.getMessage());
            return null;
        }
    }

    /** Returns screenshot bytes for attaching to Allure / Extent reports. */
    public static byte[] captureBytes(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            LoggerHandler.error("Screenshot bytes failed: " + e.getMessage());
            return new byte[0];
        }
    }
}
