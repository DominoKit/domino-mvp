package org.dominokit.domino.api.server;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class SecretKey {
    private static final Logger LOGGER= LoggerFactory.getLogger(SecretKey.class);
    public static final String HMAC_SHA_256 = "HmacSHA256";
    public static final int KEY_LENGTH = 256;

    public static String generate(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance(HMAC_SHA_256);
            generator.init(KEY_LENGTH);

            return new String(generator.generateKey().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("error while generating secret key, Random secret will be generated", e);
            return UUID.randomUUID().toString();
        }
    }
}
