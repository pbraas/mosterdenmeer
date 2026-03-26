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

    public void sendContactMail(String name, String email, String subject, String message) throws MessagingException {
        String smtpHost = getRequiredSystemProperty("SMTP_HOST");
        String smtpPort = getSystemPropertyOrDefault("SMTP_PORT", "587");
        String smtpUser = getRequiredSystemProperty("SMTP_USER");
        String smtpPassword = getRequiredSystemProperty("SMTP_PASSWORD");
        String mailTo = getRequiredSystemProperty("MAIL_TO");

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(smtpUser));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
        mimeMessage.setReplyTo(InternetAddress.parse(email));
        mimeMessage.setSubject("Contactformulier: " + subject, "UTF-8");
        mimeMessage.setText(
                "Naam: " + name + "\n"
                        + "Email: " + email + "\n"
                        + "Onderwerp: " + subject + "\n\n"
                        + message,
                "UTF-8");

        Transport.send(mimeMessage);
    }

    private String getRequiredSystemProperty(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            throw new IllegalStateException("Missing JVM option -D" + key + " for mail configuration.");
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

