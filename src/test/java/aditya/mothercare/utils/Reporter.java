package aditya.mothercare.utils;

import aditya.mothercare.utils.Base;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Extent Reports wrapper.
 * One ExtentReports instance per suite; one ExtentTest per test method.
 *
 * Usage:
 *   Reporter.init()             ← @BeforeSuite
 *   Reporter.startTest(id, name)← @BeforeMethod
 *   Reporter.pass(msg) / fail() ← inside test
 *   Reporter.flush()            ← @AfterSuite
 */
public class Reporter {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private Reporter() {}

    public static void init() {
        String dir = Base.getConfig("report.dir", "reports");
        String timestamp = LocalDateTime.now().format(FMT);
        String reportPath = dir + "/MotherCareReport_" + timestamp + ".html";

        new java.io.File(dir).mkdirs();

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle(Base.getConfig("report.title", "Mothercare Automation Report"));
        spark.config().setReportName(Base.getConfig("report.name", "Test Execution Report"));
        spark.config().setEncoding("UTF-8");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Developer", "Aditya Krishna");
        extent.setSystemInfo("Application", "Mothercare India");
        extent.setSystemInfo("Browser", Base.getConfig("browser", "chrome"));
        extent.setSystemInfo("URL", Base.getConfig("base.url"));

        LoggerHandler.info("Extent Report initialised: " + reportPath);
    }

    public static void startTest(String testCaseId, String testCaseName) {
        ExtentTest test = extent.createTest("[" + testCaseId + "] " + testCaseName);
        testThread.set(test);
        LoggerHandler.info("Extent test created: " + testCaseId);
    }

    public static void pass(String message) {
        getTest().log(Status.PASS, message);
        LoggerHandler.info("PASS - " + message);
    }

    public static void fail(String message) {
        getTest().log(Status.FAIL, message);
        LoggerHandler.error("FAIL - " + message);
    }

    public static void info(String message) {
        getTest().log(Status.INFO, message);
        LoggerHandler.info(message);
    }

    public static void warn(String message) {
        getTest().log(Status.WARNING, message);
        LoggerHandler.warn(message);
    }

    public static void skip(String message) {
        getTest().log(Status.SKIP, message);
        LoggerHandler.warn("SKIP - " + message);
    }

    /** Attaches a screenshot (file path) to the current test in the report. */
    public static void attachScreenshot(String screenshotPath) {
        if (screenshotPath != null) {
            try {
                getTest().addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                LoggerHandler.error("Could not attach screenshot to report: " + e.getMessage());
            }
        }
    }

    /** Takes a failure screenshot and attaches it. */
    public static void failWithScreenshot(WebDriver driver, String testCaseId, String message) {
        String path = Screenshot.captureFullPage(driver, testCaseId);
        fail(message);
        attachScreenshot(path);
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
            LoggerHandler.info("Extent Report flushed.");
        }
    }

    private static ExtentTest getTest() {
        ExtentTest test = testThread.get();
        if (test == null) throw new IllegalStateException("Call Reporter.startTest() before logging.");
        return test;
    }
}
