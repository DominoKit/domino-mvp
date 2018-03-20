package org.dominokit.domino.http.server.config;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.config.HttpServerConfigurator;
import org.dominokit.domino.api.server.config.ServerConfiguration;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;

import static java.lang.Boolean.TRUE;

@AutoService(HttpServerConfigurator.class)
public class JksServerConfigurator implements HttpServerConfigurator {

    private static final String SSL_CONFIGURATION_KEY = "ssl.enabled";
    private static final String SSL_JKS_PATH = "ssl.jks.path";
    private static final String SSL_JKS_SECRET = "ssl.jks.password";
    private static final String DEFAULT_EMPTY = "";
    private static final String HTTPS_PORT = "https.port";
    private static final int DEFAULT_HTTPS_PORT = 443;

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

    private void enableSsl(ServerConfiguration configuration, HttpServerOptions options) {
        options.setSsl(TRUE);
        options.setHost("localhost");
        options.setKeyStoreOptions(new JksOptions()
                .setPath(getPath(configuration))
                .setPassword(getSecret(configuration)))
                .setPort(getPort(configuration));
    }

    private String getPath(ServerConfiguration configuration) {
        return configuration.getString(SSL_JKS_PATH);
    }

    private String getSecret(ServerConfiguration configuration) {
        return configuration.getString(SSL_JKS_SECRET);
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
        if (missingSslJksPath(configuration))
            throw new MissingJksPathInConfigurationException();
        if (missingSslJksPassword(configuration))
            throw new MissingJksPasswordInConfigurationException();
    }

    private boolean missingSslJksPath(ServerConfiguration configuration) {
        return configuration.getString(SSL_JKS_PATH, DEFAULT_EMPTY).isEmpty();
    }

    private boolean missingSslJksPassword(ServerConfiguration configuration) {
        return configuration.getString(SSL_JKS_SECRET, DEFAULT_EMPTY).isEmpty();
    }

    class MissingJksPathInConfigurationException extends RuntimeException {
    }

    class MissingJksPasswordInConfigurationException extends RuntimeException {
    }
}
