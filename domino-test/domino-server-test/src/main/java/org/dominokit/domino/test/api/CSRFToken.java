package org.dominokit.domino.test.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class CSRFToken {

    private static final Base64.Encoder BASE64 = Base64.getMimeEncoder();
    public static final int SALT_LENGTH = 32;

    private Random RAND = new SecureRandom();
    private Mac mac;

    public CSRFToken(String secret) {
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new UnauthorizedRequestException(e);
        }
    }

    public String generate() {
        byte[] salt = new byte[SALT_LENGTH];
        RAND.nextBytes(salt);

        String saltPlusToken = BASE64.encodeToString(salt) + "." + Long.toString(System.currentTimeMillis());
        String signature = BASE64.encodeToString(mac.doFinal(saltPlusToken.getBytes()));

        return saltPlusToken + "." + signature;
    }

    public static class UnauthorizedRequestException extends RuntimeException{
        public UnauthorizedRequestException(Throwable cause) {
            super(cause);
        }
    }
}
