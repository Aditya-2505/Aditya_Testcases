package aditya.mothercare.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Base class: loads config, manages WebDriver lifecycle via @BeforeMethod / @AfterMethod.
 * Uses ThreadLocal so parallel runs each get their own driver.
 */
public class Base {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    protected WebDriver driver;
    protected static Properties config = new Properties();

    static {
        loadConfig();
    }

    // ── Configuration ────────────────────────────────────────────────────────

    private static void loadConfig() {
        try (FileInputStream fis = new FileInputStream("config/config.properties")) {
            config.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config/config.properties: " + e.getMessage(), e);
        }
        String ts = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now());
        System.setProperty("log.timestamp", ts);
        System.setProperty("log.dir", config.getProperty("log.dir", "logs"));
    }

    public static String getConfig(String key) {
        String value = config.getProperty(key);
        if (value == null) throw new RuntimeException("Missing required config property: " + key);
        return value.trim();
    }

    public static String getConfig(String key, String defaultValue) {
        return config.getProperty(key, defaultValue).trim();
    }

    // ── WebDriver lifecycle ──────────────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = getConfig("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(getConfig("headless", "false"));

        LoggerHandler.info("Initialising browser: " + browser);

        switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                driver = new FirefoxDriver(opts);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (headless) opts.addArguments("--headless=new");
                opts.addArguments("--disable-notifications");
                opts.addArguments("--disable-popup-blocking");
                driver = new ChromeDriver(opts);
            }
        }

        if (Boolean.parseBoolean(getConfig("window.maximize", "true"))) {
            driver.manage().window().maximize();
        }

        int implicit = Integer.parseInt(getConfig("implicit.wait", "10"));
        int pageLoad = Integer.parseInt(getConfig("page.load.timeout", "60"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));

        DRIVER.set(driver);
        LoggerHandler.info("Browser initialised successfully.");

        openHomePage();
    }

    @AfterMethod(alwaysRun = true)
    public void quitBrowser() {
        WebDriver d = DRIVER.get();
        try {
            if (d != null) {
                d.quit();
                LoggerHandler.info("Browser closed.");
            }
        } catch (Exception e) {
            LoggerHandler.error("Error closing browser: " + e.getMessage());
        } finally {
            DRIVER.remove();
        }
    }

    // ── Navigation ───────────────────────────────────────────────────────────

    protected void openHomePage() {
        String url = getConfig("base.url");
        driver.get(url);
        LoggerHandler.info("Opened URL: " + url);
    }

    // ── Static driver accessors (used by listeners) ──────────────────────────

    public static WebDriver getDriver() {
        WebDriver d = DRIVER.get();
        if (d == null) throw new IllegalStateException("WebDriver has not been initialized.");
        return d;
    }

    public static WebDriver getDriverOrNull() {
        return DRIVER.get();
    }
}
