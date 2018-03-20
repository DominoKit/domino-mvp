package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.BaseVertxServiceDiscoveryTest;
import org.dominokit.domino.service.discovery.configuration.BaseServiceConfiguration;
import org.dominokit.domino.service.discovery.configuration.HttpEndpointConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class HttpServiceDiscoveryTest extends BaseVertxServiceDiscoveryTest {

    private static final String SERVICE_ROOT = "service root";
    private static final int SERVICE_PORT = 18080;
    private static final String ROOT = "root";
    private static final String PORT = "port";
    private static final String SSL = "ssl";
    private static final int PORT_80 = 80;

    private HttpEndpointConfiguration httpEndpointConfiguration;

    @Override
    protected void onSetup() {
        httpEndpointConfiguration = new HttpEndpointConfiguration(SERVICE_NAME, SERVICE_HOST);
    }

    private void publishHttpEndpoint(HttpEndpointConfiguration configurations) {
        publishHttpEndpoint(configurations, ar -> {
        });
    }


    private void publishHttpEndpoint(HttpEndpointConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        vertxServiceDiscovery.http().publish(configuration, handler);
    }

    private void getHttpClient(Function<Record, Boolean> filter, Handler<AsyncResult<HttpClient>> handler) {
        vertxServiceDiscovery.http().getHttpClient(filter, handler);
    }

    private void getHttpClient(Function<Record, Boolean> filter, JsonObject metadata, Handler<AsyncResult<HttpClient>> handler) {
        vertxServiceDiscovery.http().getHttpClient(filter, metadata, handler);
    }

    private void getHttpClient(JsonObject jsonFilter, Handler<AsyncResult<HttpClient>> handler) {
        vertxServiceDiscovery.http().getHttpClient(jsonFilter, handler);
    }

    private void getHttpClient(JsonObject jsonFilter, JsonObject metadata, Handler<AsyncResult<HttpClient>> handler) {
        vertxServiceDiscovery.http().getHttpClient(jsonFilter, metadata, handler);
    }

    private void getWebClient(Function<Record, Boolean> filter, Handler<AsyncResult<WebClient>> handler) {
        vertxServiceDiscovery.http().getWebClient(filter, handler);
    }

    private void getWebClient(Function<Record, Boolean> filter, JsonObject metadata, Handler<AsyncResult<WebClient>> handler) {
        vertxServiceDiscovery.http().getWebClient(filter, metadata, handler);
    }

    private void getWebClient(JsonObject jsonFilter, Handler<AsyncResult<WebClient>> handler) {
        vertxServiceDiscovery.http().getWebClient(jsonFilter, handler);
    }

    private void getWebClient(JsonObject jsonFilter, JsonObject metadata, Handler<AsyncResult<WebClient>> handler) {
        vertxServiceDiscovery.http().getWebClient(jsonFilter, metadata, handler);
    }

    @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
    public void givenServiceDiscovery_whenPublishingHttpEndpointWithNullName_thenShouldThrowException() throws Exception {
        publishHttpEndpoint(new HttpEndpointConfiguration(null, SERVICE_HOST));
    }

    @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
    public void givenServiceDiscovery_whenPublishingHttpEndpointWithEmptyName_thenShouldThrowException() throws Exception {
        publishHttpEndpoint(new HttpEndpointConfiguration(EMPTY_STRING, SERVICE_HOST));
    }

    @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
    public void givenServiceDiscovery_whenPublishingHttpEndpointWithOnlySpacesName_thenShouldThrowException() throws Exception {
        publishHttpEndpoint(new HttpEndpointConfiguration(ONLY_SPACES, SERVICE_HOST));
    }

    @Test(expected = HttpEndpointConfiguration.InvalidHttpEndpointHostException.class)
    public void givenServiceDiscovery_whenPublishingHttpEndpointWithNullHost_thenShouldThrowException() throws Exception {
        publishHttpEndpoint(new HttpEndpointConfiguration(SERVICE_NAME, null));
    }

    @Test(expected = HttpEndpointConfiguration.InvalidHttpEndpointHostException.class)
    public void givenServiceDiscovery_whenPublishingHttpEndpointWithEmptyHost_thenShouldThrowException() throws Exception {
        publishHttpEndpoint(new HttpEndpointConfiguration(SERVICE_NAME, EMPTY_STRING));
    }

    @Test(expected = HttpEndpointConfiguration.InvalidHttpEndpointHostException.class)
    public void givenServiceDiscovery_whenPublishingHttpEndpointWithOnlySpacesHost_thenShouldThrowException() throws Exception {
        publishHttpEndpoint(new HttpEndpointConfiguration(SERVICE_NAME, ONLY_SPACES));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingHttpEndpoint_thenShouldPublishHttpServiceOnTheHostWithName(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration);

        serviceDiscovery.getRecord(jsonFilter, context.asyncAssertSuccess(record -> {
            context.assertEquals(SERVICE_NAME, record.getName());
            context.assertEquals(SERVICE_HOST, record.getLocation().getString("host"));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingHttpEndpoint_thenShouldPublishHttpServiceWithDefaultConfigurations(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration);

        serviceDiscovery.getRecord(jsonFilter, context.asyncAssertSuccess(record -> {
            context.assertEquals("/", record.getLocation().getString(ROOT));
            context.assertEquals(PORT_80, record.getLocation().getInteger(PORT));
            context.assertFalse(record.getLocation().getBoolean(SSL));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingHttpEndpointWithConfigurations_thenShouldPublishHttpServiceMatchingTheseConfigurations(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration
                .root(SERVICE_ROOT)
                .port(SERVICE_PORT)
                .ssl(true)
                .metadata(metadata));

        serviceDiscovery.getRecord(jsonFilter, context.asyncAssertSuccess(record -> {
            context.assertEquals("/" + SERVICE_ROOT, record.getLocation().getString(ROOT));
            context.assertEquals(SERVICE_PORT, record.getLocation().getInteger(PORT));
            context.assertTrue(record.getLocation().getBoolean(SSL));
            context.assertEquals(VALUE, record.getMetadata().getString(KEY));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingHttpEndpoint_shouldReceiveThePublishedRecordOnTheHandler(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration
                .root(SERVICE_ROOT)
                .port(SERVICE_PORT)
                .ssl(true)
                .metadata(metadata), context.asyncAssertSuccess(record -> {

            context.assertEquals("/" + SERVICE_ROOT, record.getLocation().getString(ROOT));
            context.assertEquals(SERVICE_PORT, record.getLocation().getInteger(PORT));
            context.assertTrue(record.getLocation().getBoolean(SSL));
            context.assertEquals(VALUE, record.getMetadata().getString(KEY));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenUnPublishingHttpEndpoint_thenShouldBeUnPublished(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration,
                context.asyncAssertSuccess(record ->
                        vertxServiceDiscovery.unpublish(record, context.asyncAssertSuccess())));
    }


    @Test
    public void givenServiceDiscovery_whenPublishHttpServiceAndAskingForItsHttpClientByFilter_thenShouldGetTheClient(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration);
        getHttpClient(record -> record.getName().equals(SERVICE_NAME), context.asyncAssertSuccess(Assert::assertNotNull));
    }

    @Test
    public void givenServiceDiscovery_whenPublishHttpServiceAndAskingForItsHttpClientByFilterWithConfiguration_thenShouldGetTheClient(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration.metadata(metadata));
        getHttpClient(record -> record.getName().equals(SERVICE_NAME), metadata, context.asyncAssertSuccess(Assert::assertNotNull));
    }

    @Test
    public void givenServiceDiscovery_whenPublishHttpServiceAndAskingForItsHttpClientByJson_thenShouldGetTheClient(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration);
        getHttpClient(jsonFilter, context.asyncAssertSuccess(Assert::assertNotNull));
    }

    @Test
    public void givenServiceDiscovery_whenPublishHttpServiceAndAskingForItsHttpClientByJsonWithConfiguration_thenShouldGetTheClient(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration.metadata(metadata));
        getHttpClient(jsonFilter, metadata, context.asyncAssertSuccess(Assert::assertNotNull));
    }

    @Test
    public void givenServiceDiscovery_whenPublishHttpServiceAndAskingForItsWebClientByFilter_thenShouldGetTheClient(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration);
        getWebClient(record -> record.getName().equals(SERVICE_NAME), context.asyncAssertSuccess(Assert::assertNotNull));
    }

    @Test
    public void givenServiceDiscovery_whenPublishHttpServiceAndAskingForItsWebClientByFilterWithConfiguration_thenShouldGetTheClient(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration.metadata(metadata));
        getWebClient(record -> record.getName().equals(SERVICE_NAME), metadata, context.asyncAssertSuccess(Assert::assertNotNull));
    }

    @Test
    public void givenServiceDiscovery_whenPublishHttpServiceAndAskingForItsWebClientByJson_thenShouldGetTheClient(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration);
        getWebClient(jsonFilter, context.asyncAssertSuccess(Assert::assertNotNull));
    }

    @Test
    public void givenServiceDiscovery_whenPublishHttpServiceAndAskingForItsWebClientByJsonWithConfiguration_thenShouldGetTheClient(TestContext context) throws Exception {
        publishHttpEndpoint(httpEndpointConfiguration.metadata(metadata));
        getWebClient(jsonFilter, metadata, context.asyncAssertSuccess(Assert::assertNotNull));
    }
}
