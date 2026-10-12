package aditya.mothercare.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simple screen recorder using AWT Robot.
 * Captures screenshots at ~10 fps and saves them as a series of PNG files
 * in a test-specific folder under the recordings directory.
 *
 * No external dependencies required.
 */
public class ScreenRecording {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String BASE_DIR = Base.getConfig("recording.dir", "recordings");
    private static final int FPS = 10;

    private static ScheduledExecutorService scheduler;
    private static final AtomicBoolean recording = new AtomicBoolean(false);
    private static List<File> frames = new ArrayList<>();
    private static File frameDir;
    private static Robot robot;
    private static Rectangle screenBounds;

    private ScreenRecording() {}

    /**
     * Starts recording. Call at the beginning of a test.
     */
    public static synchronized void start(String testCaseId) {
        if (recording.get()) {
            LoggerHandler.warn("Recording already running — stopping previous before starting new.");
            stop();
        }

        try {
            robot = new Robot();
            screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

            String timestamp = LocalDateTime.now().format(FMT);
            frameDir = new File(BASE_DIR + "/" + testCaseId + "_" + timestamp);
            frameDir.mkdirs();
            frames = new ArrayList<>();

            recording.set(true);
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(ScreenRecording::captureFrame,
                    0, 1000 / FPS, TimeUnit.MILLISECONDS);

            LoggerHandler.info("Screen recording started for: " + testCaseId + " → " + frameDir.getPath());
        } catch (AWTException e) {
            LoggerHandler.error("Screen recording start failed: " + e.getMessage(), e);
        }
    }

    /**
     * Stops recording. Call in @AfterMethod or finally block.
     */
    public static synchronized void stop() {
        if (!recording.get()) return;

        recording.set(false);
        if (scheduler != null) {
            scheduler.shutdown();
            try { scheduler.awaitTermination(3, TimeUnit.SECONDS); }
            catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
            scheduler = null;
        }

        int frameCount = frames.size();
        LoggerHandler.info("Screen recording stopped. " + frameCount + " frames saved to: "
                + (frameDir != null ? frameDir.getPath() : BASE_DIR));
    }

    // ── Internal frame capture ───────────────────────────────────────────────

    private static void captureFrame() {
        if (!recording.get() || robot == null) return;
        try {
            BufferedImage image = robot.createScreenCapture(screenBounds);
            File frameFile = new File(frameDir, "frame_" + String.format("%05d", frames.size()) + ".png");
            ImageIO.write(image, "PNG", frameFile);
            frames.add(frameFile);
        } catch (Exception e) {
            LoggerHandler.error("Frame capture error: " + e.getMessage());
        }
    }
}
