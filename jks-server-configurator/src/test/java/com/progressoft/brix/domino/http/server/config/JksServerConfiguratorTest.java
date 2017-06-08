package com.progressoft.brix.domino.http.server.config;

import com.progressoft.brix.domino.api.server.config.VertxConfiguration;
import com.progressoft.brix.domino.api.server.entrypoint.VertxContext;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.lang.Boolean.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class JksServerConfiguratorTest {

    private static final String SSL_CONFIGURATION_KEY = "ssl.enabled";
    private static final String SSL_JKS_PATH = "ssl.jks.path";
    private static final String SSL_JKS_SECRET = "ssl.jks.password";
    public static final String TEST_JKS_PATH = "/some/path/to/jks";
    public static final String TEST_JKS_SECRET = "some.jks.secret";
    private static final String HTTPS_PORT = "https.port";
    private static final int DEFAULT_HTTPS_PORT = 443;
    public static final int DEFAULT_TEST_SSL_PORT = 2443;
    private Vertx vertx;
    private JsonObject config;
    private Router router;
    private VertxConfiguration configuration;
    private HttpServerOptions options;
    private VertxContext context;

    @Before
    public void setUp() throws Exception {
        vertx = Vertx.vertx();
        config = vertx.getOrCreateContext().config();
        router = Router.router(vertx);
        configuration = new VertxConfiguration(config);
        configuration.put(SSL_CONFIGURATION_KEY, FALSE);
        configuration.put(SSL_JKS_PATH, TEST_JKS_PATH);
        configuration.put(SSL_JKS_SECRET, TEST_JKS_SECRET);
        configuration.put(HTTPS_PORT, DEFAULT_HTTPS_PORT);
        options = new HttpServerOptions();
        context = new VertxContext(vertx, router, configuration);
    }

    @After
    public void tearDown() throws Exception {
        vertx.close();
    }

    @Test
    public void givenVertxContextAndHttpOptions_shouldBeAbleToCallConfigureMethod() throws Exception {
        configureServer();
        assertThat(true).isTrue();
    }

    private void configureServer() {
        new JksServerConfigurator().configureHttpServer(context,
                options);
    }

    @Test(expected = JksServerConfigurator.MissingJksPathInConfigurationException.class)
    public void givenSslEnabledInConfiguration_whenJksPathIsMissingInConfiguration_thenShouldThrowException()
            throws Exception {
        configuration.put(SSL_CONFIGURATION_KEY, TRUE);
        configuration.put(SSL_JKS_PATH, "");
        configureServer();
    }

    @Test(expected = JksServerConfigurator.MissingJksPasswordInConfigurationException.class)
    public void givenSslEnabledInConfiguration_whenJksPasswordIsMissingInConfiguration_thenShouldThrowException()
            throws Exception {
        configuration.put(SSL_CONFIGURATION_KEY, TRUE);
        configuration.put(SSL_JKS_SECRET, "");
        configureServer();
    }

    @Test
    public void givenSslEnabledInConfiguration_whenHttpsPortIsMissingInConfiguration_thenShouldUseDefaultHttpsPort()
            throws Exception {
        configuration.put(SSL_CONFIGURATION_KEY, TRUE);
        configuration.remove(HTTPS_PORT);
        configureServer();
        assertThat(options.getPort()).isEqualTo(DEFAULT_HTTPS_PORT);
    }

    @Test
    public void givenSslEnabledInConfiguration_whenHttpsPortIsSetInConfiguration_thenShouldUsePortFromConfiguration()
            throws Exception {
        configuration.put(SSL_CONFIGURATION_KEY, TRUE);
        configuration.put(HTTPS_PORT, DEFAULT_TEST_SSL_PORT);
        configureServer();
        assertThat(options.getPort()).isEqualTo(DEFAULT_TEST_SSL_PORT);
    }

    @Test
    public void givenSslEnabledInConfiguration_whenConfiguringServer_thenHttpOptionSslShouldBeEnabled()
            throws Exception {
        configuration.put(SSL_CONFIGURATION_KEY, TRUE);
        configureServer();
        assertThat(options.isSsl()).isTrue();
    }

    @Test
    public void givenSslDisabledInConfigurationAndHttpOptionsSslIsDisabled_whenConfiguringServer_thenHttpOptionSslShouldRemainDisabled()
            throws Exception {
        configuration.put(SSL_CONFIGURATION_KEY, FALSE);
        options.setSsl(FALSE);
        configureServer();
        assertThat(options.isSsl()).isFalse();
    }

    @Test
    public void givenSslDisabledInConfigurationAndHttpOptionsSslIsEnabled_whenConfiguringServer_thenHttpOptionSslShouldRemainEnabled()
            throws Exception {
        configuration.put(SSL_CONFIGURATION_KEY, FALSE);
        options.setSsl(TRUE);
        configureServer();
        assertThat(options.isSsl()).isTrue();
    }

    @Test
    public void givenSslEnabledInConfigurationWithJksPathAndPassword_whenConfiguringServer_thenHttpOptionSslShouldBeEnabledAndConfiguredWithThePathAndPassword()
            throws Exception {
        configuration.put(SSL_CONFIGURATION_KEY, TRUE);
        configureServer();
        assertThat(options.isSsl()).isTrue();
        assertThat(options.getKeyStoreOptions() instanceof JksOptions).isTrue();
        assertThat(options.getKeyStoreOptions().getPath()).isEqualTo(TEST_JKS_PATH);
        assertThat(options.getKeyStoreOptions().getPassword()).isEqualTo(TEST_JKS_SECRET);
    }
}
