package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.context.DefaultExecutionContext;
import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.server.handler.HandlersRepository;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.api.server.request.DefaultMultiMap;
import org.dominokit.domino.api.server.request.DefaultRequestContext;
import org.dominokit.domino.api.server.request.RequestContext;
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


        RequestContext<TestRequest> requestContext = DefaultRequestContext
                .forRequest(request)
                .requestPath("/service/" + getPath())
                .parameters(new DefaultMultiMap<>())
                .headers(new DefaultMultiMap<>())
                .build();
        routingContext = new DefaultExecutionContext<>(requestContext, new FakeResponseContext());
    }

    private String getPath() {
        return "test/path";
    }

    @Test
    public void canRegisterRequestHandler() throws Exception {
        serverApp.registerHandler(getPath(), new TestRequestHandler());
        assertNotNull(findHandler(getPath()));
    }

    private RequestHandler findHandler(String path) {
        return serverApp.handlersRepository().findHandler("/service/" + path);
    }

    @Test(expected = HandlersRepository.RequestHandlerHaveAlreadyBeenRegistered.class)
    public void givenServerApp_whenRegisteringRequestHandlerMoreThanOnce_shouldThrowException()
            throws Exception {
        serverApp.registerHandler(getPath(), new TestRequestHandler());
        serverApp.registerHandler(getPath(), new TestRequestHandler());
    }

    @Test(expected = HandlersRepository.RequestHandlerNotFound.class)
    public void givenServerApp_whenTryingToFindARequestHandlerThatHaveNotBeenRegistered_shouldThrowException()
            throws Exception {
        findHandler(getPath());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_theRequestHandlerShouldBeInvoked() throws Exception {
        serverApp.registerHandler(getPath(), new TestRequestHandler());
        serverApp.executeRequest(routingContext, new TestServerEntryPointContext());
        assertEquals("-handled", request.getTestWord());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_thenTheRequestShouldBeInterceptedBeforeCallingTheHandler() throws Exception {
        serverApp.registerHandler(getPath(), new TestRequestHandler());
        serverApp.registerInterceptor(TestRequestHandler.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new TestInterceptor());
        serverApp.executeRequest(routingContext, new TestServerEntryPointContext());
        assertEquals("-intercepted-entry-point-parameter-handled", request.getTestWord());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_thenTheRequestShouldBeInterceptedByTheGlobalInterceptorsBeforeCallingTheHandler() throws Exception {
        serverApp.registerHandler(getPath(), new TestRequestHandler());
        serverApp.registerInterceptor(TestRequestHandler.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new TestInterceptor());
        serverApp.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new TestGlobalRequestInterceptor());
        serverApp.executeRequest(routingContext, new TestServerEntryPointContext());
        assertEquals("-intercepted-entry-point-parameter-globally-intercepted-entry-point-parameter-handled", request.getTestWord());
    }

    @After
    public void tearDown() throws Exception {
        vertx.close();
    }
}
