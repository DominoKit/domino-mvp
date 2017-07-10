package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.ModuleConfigurator;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;


public class TestModule {

    public ClientApp init(ServerEntryPointContext entryPointContext) {
        return TestClientAppFactory.make(entryPointContext);
    }

    public void replacePresenter(String presenterName, TestPresenterFactory presenterFactory) {
        ((TestInMemoryPresenterRepository) ClientApp.make().getPresentersRepository())
                .replacePresenter(presenterName, presenterFactory);
    }

    public void replaceView(String presenterName, TestViewFactory viewFactory) {
        ((TestInMemoryViewRepository) ClientApp.make().getViewsRepository()).replaceView(presenterName, viewFactory);
    }

    public <T extends View> T getView(String presenterName) {
        return (T)((TestInMemoryViewRepository) ClientApp.make().getViewsRepository()).getView(presenterName);
    }

    public void configureModule(ModuleConfiguration configuration) {
        new ModuleConfigurator().configureModule(configuration);
    }

    public void run() {
        ClientApp.make().run();
    }

    public TestDominoHistory history(){
        return (TestDominoHistory) ClientApp.make().getHistory();
    }

    public <C extends Contribution> C getContribution(Class<C> contributionClass) {
        return TestClientAppFactory.contributionsRepository.getContribution(contributionClass);
    }

    public void setRoutingListener(TestServerRouter.RoutingListener routingListener){
        TestClientAppFactory.serverRouter.setRoutingListener(routingListener);
    }

    public void removeRoutingListener(){
        TestClientAppFactory.serverRouter.removeRoutingListener();
    }

    public TestResponse forRequest(Class<? extends ClientServerRequest> request){
        return new TestResponse(request);
    }

    public static class TestResponse{

        private Class<? extends ClientServerRequest> request;

        public TestResponse(Class<? extends ClientServerRequest> request) {
            this.request = request;
        }

        public void returnResponse(ServerResponse response){
            TestClientAppFactory.serverRouter.fakeResponse(request, new TestServerRouter.SuccessReply(response));
        }

        public void failWith(Throwable throwable){
            TestClientAppFactory.serverRouter.fakeResponse(request, new TestServerRouter.FailedReply(throwable));
        }


    }



}
