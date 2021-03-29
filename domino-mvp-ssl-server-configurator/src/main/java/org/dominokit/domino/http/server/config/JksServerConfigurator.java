/*
 * Copyright © 2019 Dominokit
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
package org.dominokit.domino.http.server.config;

import static java.lang.Boolean.TRUE;

import com.google.auto.service.AutoService;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import org.dominokit.domino.api.server.config.HttpServerConfigurator;
import org.dominokit.domino.api.server.config.ServerConfiguration;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

@AutoService(HttpServerConfigurator.class)
public class JksServerConfigurator implements HttpServerConfigurator {

  @Override
  public void configureHttpServer(VertxContext context, HttpServerOptions options) {
    if (jksEnabled(context.config())) {
      applyConfigurations(context.config(), options);
    }
  }

  private boolean jksEnabled(ServerConfiguration config) {
    return config.getBoolean(ConfigKies.SSL_JKS_ENABLED, false);
  }

  private void applyConfigurations(ServerConfiguration configuration, HttpServerOptions options) {
    validateConfiguration(configuration);
    if (sslEnabled(configuration)) {
      enableSsl(configuration, options);
    }
  }

  private void enableSsl(ServerConfiguration configuration, HttpServerOptions options) {
    options.setSsl(TRUE);
    options.setHost("localhost");
    options
        .setKeyStoreOptions(
            new JksOptions().setPath(getPath(configuration)).setPassword(getSecret(configuration)))
        .setPort(getPort(configuration));
  }

  private String getPath(ServerConfiguration configuration) {
    return configuration.getString(ConfigKies.SSL_JKS_PATH);
  }

  private String getSecret(ServerConfiguration configuration) {
    return configuration.getString(ConfigKies.SSL_JKS_SECRET);
  }

  private int getPort(ServerConfiguration configuration) {
    return configuration.getInteger(ConfigKies.HTTPS_PORT, ConfigKies.DEFAULT_HTTPS_PORT);
  }

  private void validateConfiguration(ServerConfiguration configuration) {
    if (sslEnabled(configuration)) validateSslPathAndPassword(configuration);
  }

  private Boolean sslEnabled(ServerConfiguration configuration) {
    return configuration.getBoolean(ConfigKies.SSL_CONFIGURATION_KEY, false);
  }

  private void validateSslPathAndPassword(ServerConfiguration configuration) {
    if (missingSslJksPath(configuration)) throw new MissingJksPathInConfigurationException();
    if (missingSslJksPassword(configuration))
      throw new MissingJksPasswordInConfigurationException();
  }

  private boolean missingSslJksPath(ServerConfiguration configuration) {
    return configuration.getString(ConfigKies.SSL_JKS_PATH, ConfigKies.DEFAULT_EMPTY).isEmpty();
  }

  private boolean missingSslJksPassword(ServerConfiguration configuration) {
    return configuration.getString(ConfigKies.SSL_JKS_SECRET, ConfigKies.DEFAULT_EMPTY).isEmpty();
  }

  class MissingJksPathInConfigurationException extends RuntimeException {}

  class MissingJksPasswordInConfigurationException extends RuntimeException {}
}
