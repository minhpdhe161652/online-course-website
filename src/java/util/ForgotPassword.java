package util;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForgotPassword {
    public static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    static Random random = new Random();
    private static final Logger LOGGER = Logger.getLogger(ForgotPassword.class.getName());
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_PORT = "587";
    
    // Email credentials should be moved to a secure configuration file or environment variables
    private static final String EMAIL_ACCOUNT = System.getenv("EMAIL_ACCOUNT");
    private static final String EMAIL_PASSWORD = System.getenv("EMAIL_PASSWORD");

    public String getNewPassword() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }
}
