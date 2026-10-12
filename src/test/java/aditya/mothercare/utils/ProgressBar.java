package aditya.mothercare.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Simple Swing progress bar that tracks test execution progress.
 * Updates on pass, fail, or skip. Thread-safe via SwingUtilities.invokeLater.
 */
public class ProgressBar {

    private static final int TOTAL_TESTS = 16;

    private static JFrame frame;
    private static JProgressBar bar;
    private static JLabel statusLabel;
    private static int completed = 0;
    private static int passed = 0;
    private static int failed = 0;
    private static int skipped = 0;

    private ProgressBar() {}

    public static synchronized void init() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Mothercare Test Progress");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 120);
            frame.setLayout(new BorderLayout(5, 5));
            frame.setAlwaysOnTop(true);

            bar = new JProgressBar(0, TOTAL_TESTS);
            bar.setStringPainted(true);
            bar.setString("0 / " + TOTAL_TESTS);

            statusLabel = new JLabel("Running tests... (P:0  F:0  S:0)", SwingConstants.CENTER);
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            frame.add(new JLabel("  Mothercare Automation - " + TOTAL_TESTS + " test cases", SwingConstants.CENTER), BorderLayout.NORTH);
            frame.add(bar, BorderLayout.CENTER);
            frame.add(statusLabel, BorderLayout.SOUTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static synchronized void recordPass() {
        passed++;
        update();
    }

    public static synchronized void recordFail() {
        failed++;
        update();
    }

    public static synchronized void recordSkip() {
        skipped++;
        update();
    }

    private static void update() {
        completed++;
        final int c = completed;
        final int p = passed;
        final int f = failed;
        final int s = skipped;

        SwingUtilities.invokeLater(() -> {
            bar.setValue(c);
            bar.setString(c + " / " + TOTAL_TESTS);
            statusLabel.setText("Passed: " + p + "   Failed: " + f + "   Skipped: " + s);
            if (c >= TOTAL_TESTS) {
                bar.setForeground(f == 0 ? Color.GREEN : Color.RED);
            }
        });
    }

    public static void close() {
        SwingUtilities.invokeLater(() -> {
            if (frame != null) frame.dispose();
        });
    }
}
