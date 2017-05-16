package com.progressoft.brix.domino.api.server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ServerAppTest {

    private ServerApp serverApp;
    private RequestExecutor requestExecutor;
    private HandlersRepository handlersRepository;
    private InterceptorsRepository interceptorsRepository;
    private TestRequest request;
    private ServerConfiguration testConfiguration;

    @Before
    public void setUp() throws Exception {
        handlersRepository = new InMemoryHandlersRepository();
        interceptorsRepository = new InMemoryInterceptorsRepository();
        requestExecutor = new DefaultRequestExecutor(handlersRepository, interceptorsRepository);
        testConfiguration=new TestConfiguration();
        serverApp = new ServerApp.ServerAppBuilder().executor(requestExecutor).handlersRepository(handlersRepository).serverContext(
                new ServerContext() {
                    @Override
                    public ServerConfiguration config() {
                        return testConfiguration;
                    }

                    @Override
                    public void publishEndPoint(String path, EndpointsRegistry.EndpointHandlerFactory factory) {
                        //in test we do nothing
                    }

                    @Override
                    public void publishService(String path, EndpointsRegistry.EndpointHandlerFactory factory) {

                    }

                    @Override
                    public <T> T cast(Class<T> klass) throws InvalidContextTypeException {
                        return null;
                    }
                }).interceptorsRepository(interceptorsRepository).build().run();
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
        TestResponse response = (TestResponse) serverApp.executeRequest(request,
                new TestServerEntryPointContext());
        assertEquals("-handled", request.getTestWord());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_thenTheRequestShouldBeInterceptedBeforeCallingTheHandler() throws Exception {
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
        serverApp.registerInterceptor(TestRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new TestInterceptor());
        TestResponse response = (TestResponse) serverApp.executeRequest(request, new TestServerEntryPointContext());
        assertEquals("-intercepted-entry-point-parameter-handled", request.getTestWord());
    }

    @Test
    public void givenServerApp_whenExecutingARequest_thenTheRequestShouldBeInterceptedByTheGlobalInterceptorsBeforeCallingTheHandler() throws Exception {
        serverApp.registerHandler(TestRequest.class.getCanonicalName(), new TestRequestHandler());
        serverApp.registerInterceptor(TestRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new TestInterceptor());
        serverApp.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new TestGlobalRequestInterceptor());
        TestResponse response = (TestResponse) serverApp.executeRequest(request, new TestServerEntryPointContext());
        assertEquals("-intercepted-entry-point-parameter-globally-intercepted-entry-point-parameter-handled", request.getTestWord());
    }
}
