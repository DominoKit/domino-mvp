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
import static org.dominokit.domino.http.server.config.ConfigKies.*;

import com.google.auto.service.AutoService;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import org.dominokit.domino.api.server.config.HttpServerConfigurator;
import org.dominokit.domino.api.server.config.ServerConfiguration;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

@AutoService(HttpServerConfigurator.class)
public class PemCertificateConfigurator implements HttpServerConfigurator {

  private static final String CERTIFICAT_PATH = "app.ssl.certificate.path";

  @Override
  public void configureHttpServer(VertxContext context, HttpServerOptions options) {
    if (pemEnabled(context.config())) {
      applyConfigurations(context.config(), options);
    }
  }

  private boolean pemEnabled(ServerConfiguration config) {
    return config.getBoolean(SSL_PEM_ENABLED, false);
  }

  private void applyConfigurations(ServerConfiguration configuration, HttpServerOptions options) {
    validateConfiguration(configuration);
    if (sslEnabled(configuration)) {
      enableSsl(configuration, options);
    }
  }

  private void enableSsl(ServerConfiguration config, HttpServerOptions options) {
    options.setSsl(TRUE);
    options.setHost("localhost");
    options
        .setPemKeyCertOptions(
            new PemKeyCertOptions().setCertPath(getPath(config)).setKeyPath(getPath(config)))
        .setPort(getPort(config));
  }

  private String getPath(ServerConfiguration configuration) {
    return configuration.getString(CERTIFICAT_PATH);
  }

  private int getPort(ServerConfiguration configuration) {
    return configuration.getInteger(HTTPS_PORT, DEFAULT_HTTPS_PORT);
  }

  private void validateConfiguration(ServerConfiguration configuration) {
    if (sslEnabled(configuration)) validateSslPathAndPassword(configuration);
  }

  private Boolean sslEnabled(ServerConfiguration configuration) {
    return configuration.getBoolean(SSL_CONFIGURATION_KEY, false);
  }

  private void validateSslPathAndPassword(ServerConfiguration configuration) {
    if (missingCertificatePath(configuration))
      throw new PemCertificateConfigurator.MissingCertificatePathInConfigurationException();
  }

  private boolean missingCertificatePath(ServerConfiguration configuration) {
    return configuration.getString(CERTIFICAT_PATH, DEFAULT_EMPTY).isEmpty();
  }

  class MissingCertificatePathInConfigurationException extends RuntimeException {}
}
