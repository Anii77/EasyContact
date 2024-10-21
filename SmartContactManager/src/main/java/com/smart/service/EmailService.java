package com.smart.service;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // Fixed "from" email for all cases
    private final String from = "anumoynandyanunan2019@gmail.com";

    public boolean sendEmail(String message, String subject, String to) {
        String host = "smtp.gmail.com";

        // Set mail server properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Step 1: Get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Replace with your actual email and generated password (or app password)
                return new PasswordAuthentication(from, "GENERATED_PASSWORD");
            }
        });

        session.setDebug(true);

        // Step 2: Compose the message
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(message, "text/html");

            // Step 3: Send the message using Transport class
            Transport.send(mimeMessage);
            System.out.println("Sent successfully...");
            return true;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
