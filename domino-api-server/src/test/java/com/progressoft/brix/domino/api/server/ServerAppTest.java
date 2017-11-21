package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.context.DefaultExecutionContext;
import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.server.handler.HandlersRepository;
import com.progressoft.brix.domino.api.server.request.DefaultMultiValuesMap;
import com.progressoft.brix.domino.api.server.request.DefaultRequestContext;
import com.progressoft.brix.domino.api.server.request.RequestContext;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(VertxUnitRunner.class)
public class ServerAppTest {

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    private ServerApp serverApp;
    private TestRequest request;
    private Vertx vertx;
    private ExecutionContext<TestRequest, TestResponse> routingContext;

    @Before
    public void setUp() throws Exception {
        vertx = rule.vertx();
        JsonObject config = new JsonObject();
        config.put("http.port", 0);
        String secretKey = SecretKey.generate();
        RouterConfigurator configurator = new RouterConfigurator(vertx, config, secretKey);
        DominoLauncher.routerHolder.router = configurator.configuredRouter();
        DominoLauncher.configHolder.config = config;
        new DominoLoader(vertx, DominoLauncher.routerHolder.router, DominoLauncher.configHolder.config).start();
        serverApp = ServerApp.make();
        request = new TestRequest();

        request.setRequestKey(TestRequest.class.getCanonicalName());
        RequestContext<TestRequest> requestContext = new DefaultRequestContext<>(request, new DefaultMultiValuesMap<>(), new DefaultMultiValuesMap<>());
        routingContext = new DefaultExecutionContext<>(requestContext, new FakeResponseContext());
    }

    @Test
    public void canRegisterRequestHandler() throws Exception {
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
        assertNotNull(serverApp.handlersRepository().findHandler(TestRequest.class.getCanonicalName()));
    }

    @Test(expected = HandlersRepository.RequestHandlerHaveAlreadyBeenRegistered.class)
    public void givenServerApp_whenRegisteringRequestHandlerMoreThanOnce_shouldThrowException()
            throws Exception {
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
    }

    @Test(expected = HandlersRepository.RequestHandlerNotFound.class)
    public void givenServerApp_whenTryingToFindARequestHandlerThatHaveNotBeenRegistered_shouldThrowException()
            throws Exception {
        serverApp.handlersRepository().findHandler(TestRequest.class.getCanonicalName());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_theRequestHandlerShouldBeInvoked() throws Exception {
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
        serverApp.executeRequest(routingContext, new TestServerEntryPointContext());
        assertEquals("-handled", request.getTestWord());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_thenTheRequestShouldBeInterceptedBeforeCallingTheHandler() throws Exception {
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
        serverApp.registerInterceptor(TestRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new TestInterceptor());
        serverApp.executeRequest(routingContext, new TestServerEntryPointContext());
        assertEquals("-intercepted-entry-point-parameter-handled", request.getTestWord());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_thenTheRequestShouldBeInterceptedByTheGlobalInterceptorsBeforeCallingTheHandler() throws Exception {
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
        serverApp.registerInterceptor(TestRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new TestInterceptor());
        serverApp.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new TestGlobalRequestInterceptor());
        serverApp.executeRequest(routingContext, new TestServerEntryPointContext());
        assertEquals("-intercepted-entry-point-parameter-globally-intercepted-entry-point-parameter-handled", request.getTestWord());
    }

    @After
    public void tearDown() throws Exception {
        vertx.close();
    }
}
