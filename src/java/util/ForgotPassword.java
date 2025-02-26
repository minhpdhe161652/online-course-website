package util;

import java.security.SecureRandom;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class ForgotPassword {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 6; // Độ dài chuỗi là 6 ký tự

    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

    // Gửi email với chuỗi ngẫu nhiên
    public static void sendEmail(String toEmail, String randomString) {
        // Cấu hình thông tin server gửi email
        String host = "smtp.gmail.com"; // Ví dụ sử dụng Gmail
        String fromEmail = "onlinels.smtp@gmail.com"; // Email của bạn
        String password = "dqvhgyeaxdwpqtnm"; // Mật khẩu email của bạn

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Tạo phiên làm việc với email
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your Reset OTP");
            message.setText("Here is your reset OTP: " + randomString);

            // Gửi email
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        sendEmail("toiladuygg@gmail.com", "abc123");
    }
}
