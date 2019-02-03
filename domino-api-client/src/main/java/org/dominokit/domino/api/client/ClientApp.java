package org.dominokit.domino.api.client;

import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.events.EventsBus;
import org.dominokit.domino.api.client.extension.ContextAggregator;
import org.dominokit.domino.api.client.extension.DominoEventsListenersRepository;
import org.dominokit.domino.api.client.extension.DominoEventsRegistry;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.api.client.request.RequestRouter;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.client.startup.AsyncClientStartupTask;
import org.dominokit.domino.api.client.startup.ClientStartupTask;
import org.dominokit.domino.api.client.startup.TasksAggregator;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;
import org.dominokit.domino.api.shared.history.AppHistory;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;

public class ClientApp implements InitialTaskRegistry, DominoEventsRegistry {

    private static final AttributeHolder<RequestRouter<PresenterCommand>> CLIENT_ROUTER_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<RequestRouter<ServerRequest>> SERVER_ROUTER_HOLDER =
            new AttributeHolder<>();
    private static final AttributeHolder<EventsBus> EVENTS_BUS_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<DominoEventsListenersRepository> LISTENERS_REPOSITORY_HOLDER =
            new AttributeHolder<>();
    private static final AttributeHolder<AppHistory> HISTORY_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<List<ClientStartupTask>> INITIAL_TASKS_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<AsyncRunner> ASYNC_RUNNER_HOLDER = new AttributeHolder<>();
    private static final AttributeHolder<DominoOptions> DOMINO_OPTIONS_HOLDER = new AttributeHolder<>();


    private static ClientApp instance = new ClientApp();

    private ClientApp() {
    }

    @Override
    public void addListener(Class<? extends DominoEvent> event, DominoEventListener dominoEventListener) {
        LISTENERS_REPOSITORY_HOLDER.attribute.addListener(event, dominoEventListener);
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

    public AsyncRunner getAsyncRunner() {
        return ASYNC_RUNNER_HOLDER.attribute;
    }

    public AppHistory getHistory() {
        return HISTORY_HOLDER.attribute;
    }

    public DominoOptions dominoOptions() {
        return DOMINO_OPTIONS_HOLDER.attribute;
    }

    public void registerEventListener(Class<? extends DominoEvent> event, DominoEventListener listener){
        LISTENERS_REPOSITORY_HOLDER.attribute.addListener(event, listener);
    }

    public void removeEventListener(Class<? extends DominoEvent> event, DominoEventListener listener){
        LISTENERS_REPOSITORY_HOLDER.attribute.removeListener(event, listener);
    }

    public void configureModule(ModuleConfiguration configuration) {
        configuration.registerPresenters();
        configuration.registerViews();
        configuration.registerInitialTasks(this);
    }


    public void run() {
        run(canSetDominoOptions -> {
        });
    }

    public void run(DominoOptionsHandler dominoOptionsHandler) {
        dominoOptionsHandler.onBeforeRun(dominoOptions());
        dominoOptions().applyOptions();

        List<AsyncClientStartupTask> waitingList = new ArrayList<>();
        INITIAL_TASKS_HOLDER.attribute.forEach(clientStartupTask -> {
            if (clientStartupTask instanceof AsyncClientStartupTask) {
                waitingList.add((AsyncClientStartupTask) clientStartupTask);
            }
        });

        if (!waitingList.isEmpty()) {
            TreeSet<TasksAggregator> sorted = waitingList.stream().collect(groupingBy(AsyncClientStartupTask::order))
                    .entrySet().stream()
                    .map(taskEntry -> new TasksAggregator(taskEntry.getKey(), taskEntry.getValue()))
                    .collect(Collectors.toCollection(TreeSet::new));

            Iterator<TasksAggregator> iterator = sorted.iterator();
            TasksAggregator current = iterator.next();
            while (nonNull(current) && iterator.hasNext()) {
                TasksAggregator next = iterator.next();
                current.setNextAggregator(next);
                current = next;
            }

            ContextAggregator.waitFor(sorted)
                    .onReady(this::start);
            INITIAL_TASKS_HOLDER.attribute.forEach(clientStartupTask -> {
                if (!(clientStartupTask instanceof AsyncClientStartupTask)) {
                    clientStartupTask.execute();
                }
            });
            sorted.first().execute();
        } else {
            INITIAL_TASKS_HOLDER.attribute.forEach(ClientStartupTask::execute);
            start();
        }
    }

    private void start() {
        fireEvent(MainDominoEvent.class, new MainDominoEvent());
        onApplicationStarted();
    }

    private void onApplicationStarted() {
        ApplicationStartHandler applicationStartHandler = dominoOptions().getApplicationStartHandler();
        if (nonNull(applicationStartHandler)) {
            applicationStartHandler.onApplicationStarted();
        }
    }

    public void fireEvent(Class<? extends DominoEvent> extensionPointInterface,
                          DominoEvent dominoEvent) {
        LISTENERS_REPOSITORY_HOLDER.attribute.getEventListeners(extensionPointInterface)
                .forEach(c ->
                        getAsyncRunner().runAsync(() -> c.onEventReceived(dominoEvent)));
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
        HasDominoEventListenersRepository eventsListenersRepository(DominoEventsListenersRepository dominoEventsListenersRepository);
    }

    @FunctionalInterface
    public interface HasDominoEventListenersRepository {
        HasHistory history(AppHistory history);
    }

    @FunctionalInterface
    public interface HasHistory {
        HasOptions asyncRunner(AsyncRunner asyncRunner);
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
            implements HasClientRouter, HasServerRouter, HasEventBus, HasDominoEventListenersRepository,
            HasHistory, HasOptions, CanBuildClientApp {

        private RequestRouter<PresenterCommand> clientRouter;
        private RequestRouter<ServerRequest> serverRouter;
        private EventsBus eventsBus;
        private DominoEventsListenersRepository dominoEventsListenersRepository;
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
        public HasDominoEventListenersRepository eventsListenersRepository(DominoEventsListenersRepository dominoEventsListenersRepository) {
            this.dominoEventsListenersRepository = dominoEventsListenersRepository;
            return this;
        }

        @Override
        public HasHistory history(AppHistory history) {
            this.history = history;
            return this;
        }

        @Override
        public HasOptions asyncRunner(AsyncRunner asyncRunner) {
            this.asyncRunner = asyncRunner;
            return this;
        }

        @Override
        public CanBuildClientApp dominoOptions(DominoOptions dominoOptions) {
            this.dominoOptions = dominoOptions;
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
            ClientApp.LISTENERS_REPOSITORY_HOLDER.hold(dominoEventsListenersRepository);
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
    public interface DominoOptionsHandler {
        void onBeforeRun(CanSetDominoOptions dominoOptions);
    }
}
