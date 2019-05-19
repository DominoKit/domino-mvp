package org.dominokit.domino.http.server.config;

import com.google.auto.service.AutoService;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import org.dominokit.domino.api.server.config.HttpServerConfigurator;
import org.dominokit.domino.api.server.config.ServerConfiguration;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

import static java.lang.Boolean.TRUE;
import static org.dominokit.domino.http.server.config.ConfigKies.*;

@AutoService(HttpServerConfigurator.class)
public class PemCertificateConfigurator implements HttpServerConfigurator {

    private static final String CERTIFICAT_PATH = "app.ssl.certificate.path";

    @Override
    public void configureHttpServer(VertxContext context, HttpServerOptions options) {
        applyConfigurations(context.config(), options);
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
        options.setPemKeyCertOptions(new PemKeyCertOptions()
                .setCertPath(getPath(config))
                .setKeyPath(getPath(config)))
                .setPort(getPort(config));
    }

    private String getPath(ServerConfiguration configuration) {
        return configuration.getString(CERTIFICAT_PATH);
    }

    private int getPort(ServerConfiguration configuration) {
        return configuration.getInteger(HTTPS_PORT, DEFAULT_HTTPS_PORT);
    }

    private void validateConfiguration(ServerConfiguration configuration) {
        if (sslEnabled(configuration))
            validateSslPathAndPassword(configuration);
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

    class MissingCertificatePathInConfigurationException extends RuntimeException {
    }
}
