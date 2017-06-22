package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.config.ServerConfiguration;
import com.progressoft.brix.domino.api.server.config.VertxConfiguration;
import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;
import com.progressoft.brix.domino.api.server.entrypoint.ServerContext;
import com.progressoft.brix.domino.api.server.entrypoint.VertxContext;
import com.progressoft.brix.domino.api.server.handler.HandlersRepository;
import com.progressoft.brix.domino.api.server.handler.InMemoryHandlersRepository;
import com.progressoft.brix.domino.api.server.interceptor.InMemoryInterceptorsRepository;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRepository;
import com.progressoft.brix.domino.api.server.request.DefaultRequestExecutor;
import com.progressoft.brix.domino.api.server.request.RequestExecutor;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
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
    public RunTestOnContext rule= new RunTestOnContext();

    private ServerApp serverApp;
    private TestRequest request;
    private Vertx vertx;

    @Before
    public void setUp() throws Exception {

        vertx= rule.vertx();
        JsonObject config=new JsonObject();
        RouterConfigurator configurator=new RouterConfigurator(vertx);
        DominoLauncher.routerHolder.router=configurator.configuredRouter();
        DominoLauncher.configHolder.config=config;
        config.put("http.port",0);
        new DominoLoader(vertx, DominoLauncher.routerHolder.router, DominoLauncher.configHolder.config).start();
        serverApp=ServerApp.make();
        request = new TestRequest();
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
        serverApp.executeRequest(request,
                new TestServerEntryPointContext());
        assertEquals("-handled", request.getTestWord());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_thenTheRequestShouldBeInterceptedBeforeCallingTheHandler() throws Exception {
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
        serverApp.registerInterceptor(TestRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new TestInterceptor());
        serverApp.executeRequest(request, new TestServerEntryPointContext());
        assertEquals("-intercepted-entry-point-parameter-handled", request.getTestWord());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_thenTheRequestShouldBeInterceptedByTheGlobalInterceptorsBeforeCallingTheHandler() throws Exception {
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
        serverApp.registerInterceptor(TestRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new TestInterceptor());
        serverApp.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new TestGlobalRequestInterceptor());
        serverApp.executeRequest(request, new TestServerEntryPointContext());
        assertEquals("-intercepted-entry-point-parameter-globally-intercepted-entry-point-parameter-handled", request.getTestWord());
    }

    @After
    public void tearDown() throws Exception {
        vertx.close();
    }
}
