package com.progressoft.brix.domino.api.client;

import com.progressoft.brix.domino.api.client.events.EventsBus;
import com.progressoft.brix.domino.api.client.extension.Contributions;
import com.progressoft.brix.domino.api.client.extension.ContributionsRegistry;
import com.progressoft.brix.domino.api.client.extension.ContributionsRepository;
import com.progressoft.brix.domino.api.client.history.*;
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

import java.util.LinkedList;
import java.util.List;

public class ClientApp
        implements PresenterRegistry, RequestRegistry, ViewRegistry, InitialTaskRegistry, ContributionsRegistry,
        PathToRequestMapperRegistry, RequestRestSendersRegistry {

    private static final AttributeHolder<RequestRouter<ClientRequest>> CLIENT_ROUTER_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<RequestRouter<ClientServerRequest>> SERVER_ROUTER_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<EventsBus> EVENTS_BUS_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<RequestsRepository> REQUEST_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<PresentersRepository> PRESENTERS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<ViewsRepository> VIEWS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<ContributionsRepository> CONTRIBUTIONS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<PathToRequestMappersRepository> PATH_TO_REQUEST_MAPPERS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<RequestRestSendersRepository> REQUEST_REST_SENDERS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<TokenConstruct> TOKEN_CONSTRUCT_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<MainExtensionPoint> MAIN_EXTENSION_POINT_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<UrlHistory> URL_HISTORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<List<InitializeTask>> INITIAL_TASKS_HOLDER = new AttributeHolder<>();

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
    public void registerMapper(String path, RequestFromPath mapper) {
        PATH_TO_REQUEST_MAPPERS_REPOSITORY_HOLDER.attribute.registerMapper(path, mapper);
    }

    @Override
    public void registerInitialTask(InitializeTask task) {
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

    public PathToRequestMappersRepository getPathToRequestMappersRepository() {
        return PATH_TO_REQUEST_MAPPERS_REPOSITORY_HOLDER.attribute;
    }

    public RequestRestSendersRepository getRequestRestSendersRepository() {
        return REQUEST_REST_SENDERS_REPOSITORY_HOLDER.attribute;
    }

    @Override
    public void registerRequestRestSender(String requestName, LazyRequestRestSenderLoader loader) {
        REQUEST_REST_SENDERS_REPOSITORY_HOLDER.attribute.registerSender(requestName, loader);
    }

    public TokenConstruct getTokenConstruct() {
        return TOKEN_CONSTRUCT_HOLDER.attribute;
    }

    public void configureModule(ModuleConfiguration configuration) {
        configuration.registerPresenters(this);
        configuration.registerRequests(this);
        configuration.registerViews(this);
        configuration.registerContributions(this);
        configuration.registerInitialTasks(this);
        configuration.registerPathMappers(this);
        configuration.registerRequestRestSenders(this);
    }


    public void run() {
        INITIAL_TASKS_HOLDER.attribute.forEach(InitializeTask::execute);
        Contributions.apply(MainExtensionPoint.class, MAIN_EXTENSION_POINT_HOLDER.attribute);
    }

    public void applyContributions(Class<? extends ExtensionPoint> extensionPointInterface,
                                   ExtensionPoint extensionPoint) {
        CONTRIBUTIONS_REPOSITORY_HOLDER.attribute.findExtensionPointContributions(extensionPointInterface)
                .forEach(c -> c.contribute(extensionPoint));
    }

    public void applyUrlHistory() {
        URL_HISTORY_HOLDER.attribute.apply(getTokenConstruct().toUrl());
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
        HasPathToRequestMappersRepository pathToRequestMapperRepository(PathToRequestMappersRepository pathToRequestMappersRepository);
    }

    @FunctionalInterface
    public interface HasPathToRequestMappersRepository {
        HasRequestRestSendersRepository requestSendersRepository(RequestRestSendersRepository requestRestSendersRepository);
    }

    @FunctionalInterface
    public interface HasRequestRestSendersRepository {
        HasTokenConstruct tokenConstruct(TokenConstruct tokenConstruct);
    }

    @FunctionalInterface
    public interface HasTokenConstruct {
        HasUrlHistory urlHistory(UrlHistory urlHistory);
    }

    @FunctionalInterface
    public interface HasUrlHistory {
        CanBuildClientApp mainExtensionPoint(MainExtensionPoint mainExtensionPoint);
    }

    @FunctionalInterface
    public interface CanBuildClientApp {
        ClientApp build();
    }

    public static class ClientAppBuilder implements HasClientRouter, HasServerRouter, HasEventBus, HasRequestRepository, HasPresentersRepository, HasViewRepository, HasContributionsRepository, HasPathToRequestMappersRepository, HasRequestRestSendersRepository, HasTokenConstruct, HasUrlHistory, CanBuildClientApp {

        private RequestRouter<ClientRequest> clientRouter;
        private RequestRouter<ClientServerRequest> serverRouter;
        private EventsBus eventsBus;
        private RequestsRepository requestRepository;
        private PresentersRepository presentersRepository;
        private ViewsRepository viewsRepository;
        private ContributionsRepository contributionsRepository;
        private PathToRequestMappersRepository pathToRequestMappersRepository;
        private RequestRestSendersRepository requestRestSendersRepository;
        private TokenConstruct tokenConstruct;
        private MainExtensionPoint mainExtensionPoint;
        private UrlHistory urlHistory;

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
        public HasPathToRequestMappersRepository pathToRequestMapperRepository(PathToRequestMappersRepository pathToRequestMappersRepository) {
            this.pathToRequestMappersRepository = pathToRequestMappersRepository;
            return this;
        }

        @Override
        public HasRequestRestSendersRepository requestSendersRepository(RequestRestSendersRepository requestRestSendersRepository) {
            this.requestRestSendersRepository = requestRestSendersRepository;
            return this;
        }

        @Override
        public HasTokenConstruct tokenConstruct(TokenConstruct tokenConstruct) {
            this.tokenConstruct = tokenConstruct;
            return this;
        }

        @Override
        public HasUrlHistory urlHistory(UrlHistory urlHistory) {
            this.urlHistory = urlHistory;
            return this;
        }

        @Override
        public CanBuildClientApp mainExtensionPoint(MainExtensionPoint mainExtensionPoint) {
            this.mainExtensionPoint = mainExtensionPoint;
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
            ClientApp.PATH_TO_REQUEST_MAPPERS_REPOSITORY_HOLDER.hold(pathToRequestMappersRepository);
            ClientApp.REQUEST_REST_SENDERS_REPOSITORY_HOLDER.hold(requestRestSendersRepository);
            ClientApp.TOKEN_CONSTRUCT_HOLDER.hold(tokenConstruct);
            ClientApp.MAIN_EXTENSION_POINT_HOLDER.hold(mainExtensionPoint);
            ClientApp.URL_HISTORY_HOLDER.hold(urlHistory);
            ClientApp.INITIAL_TASKS_HOLDER.hold(new LinkedList<>());
        }
    }

    private static final class AttributeHolder<T> {
        private T attribute;

        public void hold(T attribute) {
            this.attribute = attribute;
        }
    }
}
