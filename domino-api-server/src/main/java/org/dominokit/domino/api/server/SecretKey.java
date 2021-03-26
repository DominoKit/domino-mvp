/*
 * Copyright © ${year} Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.api.server;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import javax.crypto.KeyGenerator;

public class SecretKey {
  private static final Logger LOGGER = LoggerFactory.getLogger(SecretKey.class);
  public static final String HMAC_SHA_256 = "HmacSHA256";
  public static final int KEY_LENGTH = 256;

  public static String generate() {
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
