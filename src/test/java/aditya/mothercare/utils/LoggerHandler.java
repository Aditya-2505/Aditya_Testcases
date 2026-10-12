package aditya.mothercare.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple wrapper around Log4j2.
 * Provides static convenience methods so every class logs the same way.
 */
public class LoggerHandler {

    private static final Logger logger = LogManager.getLogger("aditya.mothercare");

    private LoggerHandler() {}

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Throwable t) {
        logger.error(message, t);
    }

    public static void fatal(String message) {
        logger.fatal(message);
    }

    public static void fatal(String message, Throwable t) {
        logger.fatal(message, t);
    }

    /**
     * Logs test case start banner.
     */
    public static void testStart(String testCaseId, String testCaseName) {
        info("==========================================================");
        info("TEST START  [" + testCaseId + "] " + testCaseName);
        info("==========================================================");
    }

    /**
     * Logs test case end banner with result.
     */
    public static void testEnd(String testCaseId, String result) {
        info("TEST END    [" + testCaseId + "] Result: " + result);
        info("----------------------------------------------------------");
    }
}
