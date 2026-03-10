package com.company.student.app.service;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

@Service
public class TotpService {
    private static final int SECRET_SIZE = 20; // 160-bit
    private static final int TIME_STEP_SECONDS = 30;
    private static final int CODE_DIGITS = 6;
    private static final int WINDOW = 1; // previous, current, next

    public String generateSecret() {
        byte[] buffer = new byte[SECRET_SIZE];
        new SecureRandom().nextBytes(buffer);
        return new Base32().encodeToString(buffer).replace("=", "");
    }

    public boolean verifyCode(String base32Secret, String code) {
        if (base32Secret == null || code == null || !code.matches("\\d{6}")) {
            return false;
        }

        long currentTimeWindow = System.currentTimeMillis() / 1000 / TIME_STEP_SECONDS;

        for (int i = -WINDOW; i <= WINDOW; i++) {
            String candidate = generateCode(base32Secret, currentTimeWindow + i);
            if (code.equals(candidate)) {
                return true;
            }
        }
        return false;
    }

    public String generateCurrentCode(String base32Secret) {
        long currentTimeWindow = System.currentTimeMillis() / 1000 / TIME_STEP_SECONDS;
        return generateCode(base32Secret, currentTimeWindow);
    }

    private String generateCode(String base32Secret, long timeWindow) {
        try {
            Base32 base32 = new Base32();
            byte[] secretBytes = base32.decode(base32Secret);

            byte[] data = ByteBuffer.allocate(8).putLong(timeWindow).array();

            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec signKey = new SecretKeySpec(secretBytes, "HmacSHA1");
            mac.init(signKey);

            byte[] hash = mac.doFinal(data);

            int offset = hash[hash.length - 1] & 0x0F;

            int binary =
                    ((hash[offset] & 0x7F) << 24) |
                            ((hash[offset + 1] & 0xFF) << 16) |
                            ((hash[offset + 2] & 0xFF) << 8) |
                            (hash[offset + 3] & 0xFF);

            int otp = binary % (int) Math.pow(10, CODE_DIGITS);
            return String.format("%0" + CODE_DIGITS + "d", otp);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate TOTP code", e);
        }
    }

    public String buildOtpAuthUrl(String issuer, String accountName, String secret) {
        String normalizedIssuer = issuer.replace(" ", "%20");
        String normalizedAccount = accountName.replace(" ", "%20");

        return "otpauth://totp/" + normalizedIssuer + ":" + normalizedAccount
                + "?secret=" + secret
                + "&issuer=" + normalizedIssuer
                + "&algorithm=SHA1"
                + "&digits=6"
                + "&period=30";
    }
}
