package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.ModuleConfigurator;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.history.AppHistory;


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

}
