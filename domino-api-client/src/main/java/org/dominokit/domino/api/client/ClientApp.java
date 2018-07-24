package org.dominokit.domino.api.client;

import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.events.EventsBus;
import org.dominokit.domino.api.client.extension.DominoEventsRegistry;
import org.dominokit.domino.api.client.extension.DominoEventsListenersRepository;
import org.dominokit.domino.api.client.mvp.PresenterRegistry;
import org.dominokit.domino.api.client.mvp.ViewRegistry;
import org.dominokit.domino.api.client.mvp.presenter.LazyPresenterLoader;
import org.dominokit.domino.api.client.mvp.presenter.PresentersRepository;
import org.dominokit.domino.api.client.mvp.view.LazyViewLoader;
import org.dominokit.domino.api.client.mvp.view.ViewsRepository;
import org.dominokit.domino.api.client.request.*;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;
import org.dominokit.domino.api.shared.history.AppHistory;
import org.dominokit.domino.api.shared.history.DominoHistory;

import java.util.LinkedList;
import java.util.List;

public class ClientApp
        implements PresenterRegistry, CommandRegistry, ViewRegistry, InitialTaskRegistry, DominoEventsRegistry,
        RequestRestSendersRegistry {

    private static final AttributeHolder<RequestRouter<PresenterCommand>> CLIENT_ROUTER_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<RequestRouter<ServerRequest>> SERVER_ROUTER_HOLDER =
            new AttributeHolder<>();
    private static final AttributeHolder<EventsBus> EVENTS_BUS_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<CommandsRepository> COMMANDS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<PresentersRepository> PRESENTERS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<ViewsRepository> VIEWS_REPOSITORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<DominoEventsListenersRepository> LISTENERS_REPOSITORY_HOLDER =
            new AttributeHolder<>();
    private static final AttributeHolder<RequestRestSendersRepository> REQUEST_REST_SENDERS_REPOSITORY_HOLDER =
            new AttributeHolder<>();
    private static final AttributeHolder<MainDominoEvent> MAIN_EXTENSION_POINT_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<AppHistory> HISTORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<List<ClientStartupTask>> INITIAL_TASKS_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<AsyncRunner> ASYNC_RUNNER_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<DominoOptions> DOMINO_OPTIONS_HOLDER = new AttributeHolder<>();

    private static ClientApp instance=new ClientApp();

    private ClientApp() {
    }

    @Override
    public void registerPresenter(LazyPresenterLoader lazyPresenterLoader) {
        PRESENTERS_REPOSITORY_HOLDER.attribute.registerPresenter(lazyPresenterLoader);
    }

    @Override
    public void registerCommand(String commandName, String presenterName) {
        COMMANDS_REPOSITORY_HOLDER.attribute.registerCommand(new RequestHolder(commandName, presenterName));
    }

    @Override
    public void registerView(LazyViewLoader lazyViewLoader) {
        VIEWS_REPOSITORY_HOLDER.attribute.registerView(lazyViewLoader);
    }

    @Override
    public void addListener(Class<? extends DominoEvent> event, DominoEventListener dominoEventListener) {
        LISTENERS_REPOSITORY_HOLDER.attribute.addListener(event, dominoEventListener);
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
        return instance;
    }

    public RequestRouter<PresenterCommand> getClientRouter() {
        return CLIENT_ROUTER_HOLDER.attribute;
    }

    public RequestRouter<ServerRequest> getServerRouter() {
        return SERVER_ROUTER_HOLDER.attribute;
    }

    public EventsBus getEventsBus() {
        return EVENTS_BUS_HOLDER.attribute;
    }

    public CommandsRepository getRequestRepository() {
        return COMMANDS_REPOSITORY_HOLDER.attribute;
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
        configuration.registerListeners(this);
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
        fireEvent(MainDominoEvent.class, MAIN_EXTENSION_POINT_HOLDER.attribute);
    }

    public void fireEvent(Class<? extends DominoEvent> extensionPointInterface,
                          DominoEvent dominoEvent) {
        LISTENERS_REPOSITORY_HOLDER.attribute.getEventListeners(extensionPointInterface)
                .forEach(c ->
                getAsyncRunner().runAsync(() -> c.listen(dominoEvent)));
    }

    @FunctionalInterface
    public interface HasClientRouter {
        HasServerRouter serverRouter(RequestRouter<ServerRequest> serverRouter);
    }

    @FunctionalInterface
    public interface HasServerRouter {
        HasEventBus eventsBus(EventsBus eventsBus);
    }

    @FunctionalInterface
    public interface HasEventBus {
        HasRequestRepository requestRepository(CommandsRepository requestRepository);
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
        HasDominoEventListenersRepository eventsListenersRepository(DominoEventsListenersRepository dominoEventsListenersRepository);
    }

    @FunctionalInterface
    public interface HasDominoEventListenersRepository {
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
        HasOptions mainExtensionPoint(MainDominoEvent mainDominoEvent);
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
            HasViewRepository, HasDominoEventListenersRepository, HasRequestRestSendersRepository,
            HasHistory, HasAsyncRunner, HasOptions, CanBuildClientApp {

        private RequestRouter<PresenterCommand> clientRouter;
        private RequestRouter<ServerRequest> serverRouter;
        private EventsBus eventsBus;
        private CommandsRepository requestRepository;
        private PresentersRepository presentersRepository;
        private ViewsRepository viewsRepository;
        private DominoEventsListenersRepository dominoEventsListenersRepository;
        private RequestRestSendersRepository requestRestSendersRepository;
        private MainDominoEvent mainDominoEvent;
        private AppHistory history;
        private AsyncRunner asyncRunner;
        private DominoOptions dominoOptions;

        private ClientAppBuilder(RequestRouter<PresenterCommand> clientRouter) {
            this.clientRouter = clientRouter;
        }

        public static HasClientRouter clientRouter(RequestRouter<PresenterCommand> clientRouter) {
            return new ClientAppBuilder(clientRouter);
        }

        @Override
        public HasServerRouter serverRouter(RequestRouter<ServerRequest> serverRouter) {
            this.serverRouter = serverRouter;
            return this;
        }

        @Override
        public HasEventBus eventsBus(EventsBus eventsBus) {
            this.eventsBus = eventsBus;
            return this;
        }

        @Override
        public HasRequestRepository requestRepository(CommandsRepository requestRepository) {
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
        public HasDominoEventListenersRepository eventsListenersRepository(DominoEventsListenersRepository dominoEventsListenersRepository) {
            this.dominoEventsListenersRepository = dominoEventsListenersRepository;
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
        public HasOptions mainExtensionPoint(MainDominoEvent mainDominoEvent) {
            this.mainDominoEvent = mainDominoEvent;
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
            ClientApp.COMMANDS_REPOSITORY_HOLDER.hold(requestRepository);
            ClientApp.PRESENTERS_REPOSITORY_HOLDER.hold(presentersRepository);
            ClientApp.VIEWS_REPOSITORY_HOLDER.hold(viewsRepository);
            ClientApp.LISTENERS_REPOSITORY_HOLDER.hold(dominoEventsListenersRepository);
            ClientApp.REQUEST_REST_SENDERS_REPOSITORY_HOLDER.hold(requestRestSendersRepository);
            ClientApp.MAIN_EXTENSION_POINT_HOLDER.hold(mainDominoEvent);
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
