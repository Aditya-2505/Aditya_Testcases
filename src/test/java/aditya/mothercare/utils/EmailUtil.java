package aditya.mothercare.utils;

import aditya.mothercare.utils.Base;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Sends an emailable summary report after test execution.
 * Only runs when email.enabled=true in config.properties.
 * Never hardcodes credentials.
 */
public class EmailUtil {

    private EmailUtil() {}

    /**
     * Sends the test summary email if email is enabled in config.
     *
     * @param totalTests  total test count
     * @param passed      passed count
     * @param failed      failed count
     * @param skipped     skipped count
     * @param reportPath  path to the Extent HTML report
     */
    public static void sendReport(int totalTests, int passed, int failed, int skipped, String reportPath) {
        String enabled = Base.getConfig("email.enabled", "false");
        if (!"true".equalsIgnoreCase(enabled)) {
            LoggerHandler.info("Email reporting is disabled. Set email.enabled=true in config.properties to enable.");
            return;
        }

        String smtpHost = Base.getConfig("email.smtp.host");
        String smtpPort = Base.getConfig("email.smtp.port");
        String from     = Base.getConfig("email.from");
        String password = Base.getConfig("email.password");
        String to       = Base.getConfig("email.to");
        String subject  = Base.getConfig("email.subject", "Mothercare Automation Report");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            String body = buildEmailBody(totalTests, passed, failed, skipped, reportPath);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
            LoggerHandler.info("Emailable report sent to: " + to);
        } catch (MessagingException e) {
            LoggerHandler.error("Failed to send email report: " + e.getMessage(), e);
        }
    }

    private static String buildEmailBody(int total, int passed, int failed, int skipped, String reportPath) {
        return "<html><body style='font-family:Arial,sans-serif;'>"
                + "<h2>Mothercare Automation Report</h2>"
                + "<table border='1' cellpadding='8' cellspacing='0' style='border-collapse:collapse;'>"
                + "<tr><th>Total</th><th style='color:green;'>Passed</th><th style='color:red;'>Failed</th><th style='color:orange;'>Skipped</th></tr>"
                + "<tr><td>" + total + "</td><td>" + passed + "</td><td>" + failed + "</td><td>" + skipped + "</td></tr>"
                + "</table>"
                + "<p>Report location: <code>" + reportPath + "</code></p>"
                + "<p>Developer: Aditya Krishna | Application: Mothercare India</p>"
                + "</body></html>";
    }
}
