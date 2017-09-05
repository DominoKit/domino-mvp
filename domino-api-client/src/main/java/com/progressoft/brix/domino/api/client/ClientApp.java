package com.progressoft.brix.domino.api.client;

import com.progressoft.brix.domino.api.client.async.AsyncRunner;
import com.progressoft.brix.domino.api.client.events.EventsBus;
import com.progressoft.brix.domino.api.client.extension.ContributionsRegistry;
import com.progressoft.brix.domino.api.client.extension.ContributionsRepository;
import com.progressoft.brix.domino.api.client.mvp.PresenterRegistry;
import com.progressoft.brix.domino.api.client.mvp.ViewRegistry;
import com.progressoft.brix.domino.api.client.mvp.presenter.LazyPresenterLoader;
import com.progressoft.brix.domino.api.client.mvp.presenter.PresentersRepository;
import com.progressoft.brix.domino.api.client.mvp.view.LazyViewLoader;
import com.progressoft.brix.domino.api.client.mvp.view.ViewsRepository;
import com.progressoft.brix.domino.api.client.request.*;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.extension.ExtensionPoint;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;
import com.progressoft.brix.domino.api.shared.history.AppHistory;
import com.progressoft.brix.domino.api.shared.history.DominoHistory;

import java.util.LinkedList;
import java.util.List;

public class ClientApp
        implements PresenterRegistry, RequestRegistry, ViewRegistry, InitialTaskRegistry, ContributionsRegistry,
        RequestRestSendersRegistry {

    private static final AttributeHolder<RequestRouter<ClientRequest>> CLIENT_ROUTER_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<RequestRouter<ClientServerRequest>> SERVER_ROUTER_HOLDER =
            new AttributeHolder<>();
    private static final AttributeHolder<EventsBus> EVENTS_BUS_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<RequestsRepository> REQUEST_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<PresentersRepository> PRESENTERS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<ViewsRepository> VIEWS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<ContributionsRepository> CONTRIBUTIONS_REPOSITORY_HOLDER =
            new AttributeHolder<>();
    private static final AttributeHolder<RequestRestSendersRepository> REQUEST_REST_SENDERS_REPOSITORY_HOLDER =
            new AttributeHolder<>();
    private static final AttributeHolder<MainExtensionPoint> MAIN_EXTENSION_POINT_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<AppHistory> HISTORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<List<ClientStartupTask>> INITIAL_TASKS_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<AsyncRunner> ASYNC_RUNNER_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<DominoOptions> DOMINO_OPTIONS_HOLDER = new AttributeHolder<>();

    private ClientApp() {
    }

    @Override
    public void registerPresenter(LazyPresenterLoader lazyPresenterLoader) {
        PRESENTERS_REPOSITORY_HOLDER.attribute.registerPresenter(lazyPresenterLoader);
    }

    @Override
    public void registerRequest(String requestName, String presenterName) {
        REQUEST_REPOSITORY_HOLDER.attribute.registerRequest(new RequestHolder(requestName, presenterName));
    }

    @Override
    public void registerView(LazyViewLoader lazyViewLoader) {
        VIEWS_REPOSITORY_HOLDER.attribute.registerView(lazyViewLoader);
    }

    @Override
    public void registerContribution(Class<? extends ExtensionPoint> extensionPoint, Contribution contribution) {
        CONTRIBUTIONS_REPOSITORY_HOLDER.attribute.registerContribution(extensionPoint, contribution);
    }

    @Override
    public void registerRequestRestSender(String requestName, LazyRequestRestSenderLoader loader) {
        REQUEST_REST_SENDERS_REPOSITORY_HOLDER.attribute.registerSender(requestName, loader);
    }

    @Override
    public void registerInitialTask(ClientStartupTask task) {
        INITIAL_TASKS_HOLDER.attribute.add(task);
    }

    public static ClientApp make() {
        return new ClientApp();
    }

    public RequestRouter<ClientRequest> getClientRouter() {
        return CLIENT_ROUTER_HOLDER.attribute;
    }

    public RequestRouter<ClientServerRequest> getServerRouter() {
        return SERVER_ROUTER_HOLDER.attribute;
    }

    public EventsBus getEventsBus() {
        return EVENTS_BUS_HOLDER.attribute;
    }

    public RequestsRepository getRequestRepository() {
        return REQUEST_REPOSITORY_HOLDER.attribute;
    }

    public PresentersRepository getPresentersRepository() {
        return PRESENTERS_REPOSITORY_HOLDER.attribute;
    }

    public ViewsRepository getViewsRepository() {
        return VIEWS_REPOSITORY_HOLDER.attribute;
    }

    public RequestRestSendersRepository getRequestRestSendersRepository() {
        return REQUEST_REST_SENDERS_REPOSITORY_HOLDER.attribute;
    }

    public AsyncRunner getAsyncRunner() {
        return ASYNC_RUNNER_HOLDER.attribute;
    }

    public DominoHistory getHistory() {
        return HISTORY_HOLDER.attribute;
    }

    public DominoOptions dominoOptions(){
        return DOMINO_OPTIONS_HOLDER.attribute;
    }

    public void configureModule(ModuleConfiguration configuration) {
        configuration.registerPresenters(this);
        configuration.registerRequests(this);
        configuration.registerViews(this);
        configuration.registerContributions(this);
        configuration.registerInitialTasks(this);
        configuration.registerRequestRestSenders(this);
    }


    public void run() {
        run(canSetDominoOptions -> {});
    }

    public void run(DominoOptionsHandler dominoOptionsHandler) {
        dominoOptionsHandler.onBeforeRun(dominoOptions());
        dominoOptions().applyOptions();
        INITIAL_TASKS_HOLDER.attribute.forEach(ClientStartupTask::execute);
        applyContributions(MainExtensionPoint.class, MAIN_EXTENSION_POINT_HOLDER.attribute);
    }

    public void applyContributions(Class<? extends ExtensionPoint> extensionPointInterface,
                                   ExtensionPoint extensionPoint) {
        CONTRIBUTIONS_REPOSITORY_HOLDER.attribute.findExtensionPointContributions(extensionPointInterface)
                .forEach(c ->
                getAsyncRunner().runAsync(() -> c.contribute(extensionPoint)));
    }

    @FunctionalInterface
    public interface HasClientRouter {
        HasServerRouter serverRouter(RequestRouter<ClientServerRequest> serverRouter);
    }

    @FunctionalInterface
    public interface HasServerRouter {
        HasEventBus eventsBus(EventsBus eventsBus);
    }

    @FunctionalInterface
    public interface HasEventBus {
        HasRequestRepository requestRepository(RequestsRepository requestRepository);
    }

    @FunctionalInterface
    public interface HasRequestRepository {
        HasPresentersRepository presentersRepository(PresentersRepository presentersRepository);
    }

    @FunctionalInterface
    public interface HasPresentersRepository {
        HasViewRepository viewsRepository(ViewsRepository viewsRepository);
    }

    @FunctionalInterface
    public interface HasViewRepository {
        HasContributionsRepository contributionsRepository(ContributionsRepository contributionsRepository);
    }

    @FunctionalInterface
    public interface HasContributionsRepository {
        HasRequestRestSendersRepository requestSendersRepository(
                RequestRestSendersRepository requestRestSendersRepository);
    }

    @FunctionalInterface
    public interface HasRequestRestSendersRepository {
        HasHistory history(AppHistory history);
    }

    @FunctionalInterface
    public interface HasHistory {
        HasAsyncRunner asyncRunner(AsyncRunner asyncRunner);
    }

    @FunctionalInterface
    public interface HasAsyncRunner {
        HasOptions mainExtensionPoint(MainExtensionPoint mainExtensionPoint);
    }

    @FunctionalInterface
    public interface HasOptions {
        CanBuildClientApp dominoOptions(DominoOptions dominoOptions);
    }

    @FunctionalInterface
    public interface CanBuildClientApp {
        ClientApp build();
    }

    public static class ClientAppBuilder
            implements HasClientRouter, HasServerRouter, HasEventBus, HasRequestRepository, HasPresentersRepository,
            HasViewRepository, HasContributionsRepository, HasRequestRestSendersRepository,
            HasHistory, HasAsyncRunner, HasOptions, CanBuildClientApp {

        private RequestRouter<ClientRequest> clientRouter;
        private RequestRouter<ClientServerRequest> serverRouter;
        private EventsBus eventsBus;
        private RequestsRepository requestRepository;
        private PresentersRepository presentersRepository;
        private ViewsRepository viewsRepository;
        private ContributionsRepository contributionsRepository;
        private RequestRestSendersRepository requestRestSendersRepository;
        private MainExtensionPoint mainExtensionPoint;
        private AppHistory history;
        private AsyncRunner asyncRunner;
        private DominoOptions dominoOptions;

        private ClientAppBuilder(RequestRouter<ClientRequest> clientRouter) {
            this.clientRouter = clientRouter;
        }

        public static HasClientRouter clientRouter(RequestRouter<ClientRequest> clientRouter) {
            return new ClientAppBuilder(clientRouter);
        }

        @Override
        public HasServerRouter serverRouter(RequestRouter<ClientServerRequest> serverRouter) {
            this.serverRouter = serverRouter;
            return this;
        }

        @Override
        public HasEventBus eventsBus(EventsBus eventsBus) {
            this.eventsBus = eventsBus;
            return this;
        }

        @Override
        public HasRequestRepository requestRepository(RequestsRepository requestRepository) {
            this.requestRepository = requestRepository;
            return this;
        }

        @Override
        public HasPresentersRepository presentersRepository(PresentersRepository presentersRepository) {
            this.presentersRepository = presentersRepository;
            return this;
        }

        @Override
        public HasViewRepository viewsRepository(ViewsRepository viewsRepository) {
            this.viewsRepository = viewsRepository;
            return this;
        }

        @Override
        public HasContributionsRepository contributionsRepository(ContributionsRepository contributionsRepository) {
            this.contributionsRepository = contributionsRepository;
            return this;
        }

        @Override
        public HasRequestRestSendersRepository requestSendersRepository(
                RequestRestSendersRepository requestRestSendersRepository) {
            this.requestRestSendersRepository = requestRestSendersRepository;
            return this;
        }

        @Override
        public HasHistory history(AppHistory history) {
            this.history = history;
            return this;
        }

        @Override
        public HasAsyncRunner asyncRunner(AsyncRunner asyncRunner) {
            this.asyncRunner = asyncRunner;
            return this;
        }

        @Override
        public HasOptions mainExtensionPoint(MainExtensionPoint mainExtensionPoint) {
            this.mainExtensionPoint = mainExtensionPoint;
            return this;
        }

        @Override
        public CanBuildClientApp dominoOptions(DominoOptions dominoOptions) {
            this.dominoOptions=dominoOptions;
            return this;
        }

        @Override
        public ClientApp build() {
            initClientApp();
            return new ClientApp();
        }

        private void initClientApp() {
            ClientApp.CLIENT_ROUTER_HOLDER.hold(clientRouter);
            ClientApp.SERVER_ROUTER_HOLDER.hold(serverRouter);
            ClientApp.EVENTS_BUS_HOLDER.hold(eventsBus);
            ClientApp.REQUEST_REPOSITORY_HOLDER.hold(requestRepository);
            ClientApp.PRESENTERS_REPOSITORY_HOLDER.hold(presentersRepository);
            ClientApp.VIEWS_REPOSITORY_HOLDER.hold(viewsRepository);
            ClientApp.CONTRIBUTIONS_REPOSITORY_HOLDER.hold(contributionsRepository);
            ClientApp.REQUEST_REST_SENDERS_REPOSITORY_HOLDER.hold(requestRestSendersRepository);
            ClientApp.MAIN_EXTENSION_POINT_HOLDER.hold(mainExtensionPoint);
            ClientApp.HISTORY_HOLDER.hold(history);
            ClientApp.INITIAL_TASKS_HOLDER.hold(new LinkedList<>());
            ClientApp.ASYNC_RUNNER_HOLDER.hold(asyncRunner);
            ClientApp.DOMINO_OPTIONS_HOLDER.hold(dominoOptions);
        }


    }

    private static final class AttributeHolder<T> {
        private T attribute;

        public void hold(T attribute) {
            this.attribute = attribute;
        }
    }

    @FunctionalInterface
    public interface DominoOptionsHandler{
        void onBeforeRun(CanSetDominoOptions dominoOptions);
    }
}
