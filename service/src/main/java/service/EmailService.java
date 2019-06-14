package service;

import exceptions.AppException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String emailAddress = "pan2xxx19112@gmail.com";
    private static final String emailPassword = "mateusz1911";

    public String convertToHtml(String message) {
        return "<h1>HELLO WORLD</h1>";
    }

    public void sendAsHtml(String recipient, String subject, String htmlMessage) {

        try {
            System.out.println("Sending email message ...");

            Session session = createSession();

            MimeMessage mimeMessage = new MimeMessage(session);
            prepareEmailMessage(mimeMessage, recipient, subject, convertToHtml(htmlMessage));

            Transport.send(mimeMessage);
            System.out.println("Email has been sent.");
        } catch (Exception e) {
            throw new AppException("Send as html exception");
        }
    }

    private static void prepareEmailMessage(MimeMessage mimeMessage, String recipient, String subject, String htmlMessage) {

        try {
            mimeMessage.setContent(htmlMessage, "text/html; charset=utf-8");
            mimeMessage.setFrom(new InternetAddress(emailAddress));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            mimeMessage.setSubject(subject);
        } catch (Exception e) {
            throw new AppException("Prepare Email Message Exception");
        }
    }

    private static Session createSession() {

        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        return Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailAddress, emailPassword);
                    }
                });
    }

}
