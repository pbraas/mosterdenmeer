package nl.mosterdenmeer.mosterdenmeer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

@ApplicationScoped
public class ContactMailService {
    /**
     * If the given system property is set, put it in the properties object.
     */
    private void maybePutProperty(Properties properties, String key) {
        String value = System.getProperty(key);
        if (!isBlank(value)) {
            properties.put(key, value);
        }
    }

    public void sendContactMail(String name, String email, String subject, String message) throws MessagingException {
        // Get properties from system or use defaults
        String smtpHost = "ns1.hoekschlyceum-server.nl";
        String smtpPort = getSystemPropertyOrDefault("SMTP_PORT", "587");
        String smtpUser = "info@mosterdenmeer.nl";
        String smtpPassword = "NfzHg7m67jWmSPJgffdq";
        String mailTo = "info@mosterdenmeer.nl";
        String mailCc = "phillipbraas@gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.transport.protocol", getSystemPropertyOrDefault("mail.transport.protocol", "smtp"));
        properties.put("mail.smtp.auth", getSystemPropertyOrDefault("mail.smtp.auth", "true"));
        properties.put("mail.smtp.starttls.enable", getSystemPropertyOrDefault("mail.smtp.starttls.enable", "true"));
        properties.put("mail.smtp.user", smtpUser);
        properties.put("mail.smtp.password", smtpPassword);

        // SSL trust: always trust the hardcoded host
        properties.put("mail.smtp.ssl.trust", smtpHost);

        // TEMPORARY WORKAROUND: Trust all certificates and disable hostname checking (not secure for production)
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.ssl.checkserveridentity", "false");

        maybePutProperty(properties, "mail.smtp.ssl.enable");
        maybePutProperty(properties, "mail.store.protocol");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(smtpUser));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
        if (!isBlank(mailCc)) {
            mimeMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mailCc));
        }
        mimeMessage.setReplyTo(InternetAddress.parse(email));
        mimeMessage.setSubject("Van Mosterd en Meer contactformulier: " + subject, "UTF-8");
        mimeMessage.setText(
                "Naam: " + name + "\n"
                        + "Email: " + email + "\n"
                        + "Onderwerp: " + subject + "\n\n"
                        + message,
                "UTF-8");

        // Use explicit Transport for more control and error handling
        try {
            Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        } catch (MessagingException e) {
            // Log the full exception for diagnosis
            e.printStackTrace(); // You can replace this with a logger if available
            throw e;
        }
    }

    /**
     * Get a required system property, throw if missing or blank.
     */
    private String getRequiredSystemProperty(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            throw new IllegalStateException("Missing required system property: " + key);
        }
        return value;
    }

    private String getSystemPropertyOrDefault(String key, String fallback) {
        String value = System.getProperty(key);
        return isBlank(value) ? fallback : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
