/*
 * Copyright © ${year} Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.api.client;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;

import java.util.*;
import java.util.stream.Collectors;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.extension.DominoEventsListenersRepository;
import org.dominokit.domino.api.client.extension.DominoEventsRegistry;
import org.dominokit.domino.api.client.mvp.slots.SlotsManager;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.api.client.startup.AsyncClientStartupTask;
import org.dominokit.domino.api.client.startup.BaseRoutingStartupTask;
import org.dominokit.domino.api.client.startup.ClientStartupTask;
import org.dominokit.domino.api.client.startup.TasksAggregator;
import org.dominokit.domino.api.shared.extension.*;
import org.dominokit.domino.history.AppHistory;
import org.dominokit.domino.rest.shared.EventsBus;
import org.dominokit.domino.rest.shared.request.RequestRouter;

public class ClientApp implements InitialTaskRegistry, DominoEventsRegistry {

  private String name;

  private static final AttributeHolder<RequestRouter<PresenterCommand>> CLIENT_ROUTER_HOLDER =
      new AttributeHolder<>();
  private static final AttributeHolder<EventsBus> EVENTS_BUS_HOLDER = new AttributeHolder<>();
  private static final AttributeHolder<DominoEventsListenersRepository>
      LISTENERS_REPOSITORY_HOLDER = new AttributeHolder<>();
  private static final AttributeHolder<AppHistory> HISTORY_HOLDER = new AttributeHolder<>();
  private static final AttributeHolder<List<ClientStartupTask>> INITIAL_TASKS_HOLDER =
      new AttributeHolder<>();
  private static final AttributeHolder<AsyncRunner> ASYNC_RUNNER_HOLDER = new AttributeHolder<>();
  private static final AttributeHolder<DominoOptions> DOMINO_OPTIONS_HOLDER =
      new AttributeHolder<>();
  private static final AttributeHolder<SlotsManager> SLOT_MANAGER_HOLDER = new AttributeHolder<>();

  private List<ModuleConfiguration> modules = new ArrayList<>();

  private static ClientApp instance = new ClientApp();

  private ClientApp() {}

  @Override
  public void addListener(
      Class<? extends DominoEvent> event, DominoEventListener dominoEventListener) {
    LISTENERS_REPOSITORY_HOLDER.attribute.addListener(event, dominoEventListener);
  }

  @Override
  public void registerInitialTask(ClientStartupTask task) {
    INITIAL_TASKS_HOLDER.attribute.add(task);
  }

  public static ClientApp make() {
    return instance;
  }

  public static ClientApp make(String name) {
    instance.name = name;
    return instance;
  }

  public String getName() {
    return instance.name;
  }

  public RequestRouter<PresenterCommand> getClientRouter() {
    return CLIENT_ROUTER_HOLDER.attribute;
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

  public SlotsManager slotsManager() {
    return SLOT_MANAGER_HOLDER.attribute;
  }

  public void registerEventListener(
      Class<? extends DominoEvent> event, DominoEventListener listener) {
    LISTENERS_REPOSITORY_HOLDER.attribute.addListener(event, listener);
  }

  public void registerGlobalEventListener(
      Class<? extends DominoEvent> event, GlobalDominoEventListener listener) {
    LISTENERS_REPOSITORY_HOLDER.attribute.addGlobalListener(event, listener);
  }

  public void removeEventListener(
      Class<? extends DominoEvent> event, DominoEventListener listener) {
    LISTENERS_REPOSITORY_HOLDER.attribute.removeListener(event, listener);
  }

  public void removeGlobalEventListener(
      Class<? extends DominoEvent> event, GlobalDominoEventListener listener) {
    LISTENERS_REPOSITORY_HOLDER.attribute.removeGlobalListener(event, listener);
  }

  public void configureModule(ModuleConfiguration configuration) {
    modules.add(configuration);
  }

  public void run() {
    run(canSetDominoOptions -> {});
  }

  public void run(DominoOptionsHandler dominoOptionsHandler) {
    modules.forEach(
        configuration -> {
          configuration.registerPresenters();
          configuration.registerViews();
          configuration.registerInitialTasks(this);
        });

    modules.clear();

    dominoOptionsHandler.onBeforeRun(dominoOptions());
    dominoOptions().applyOptions();

    List<AsyncClientStartupTask> waitingList = new ArrayList<>();
    INITIAL_TASKS_HOLDER.attribute.forEach(
        clientStartupTask -> {
          if (clientStartupTask instanceof AsyncClientStartupTask) {
            waitingList.add((AsyncClientStartupTask) clientStartupTask);
          }
        });

    if (!waitingList.isEmpty()) {
      TreeSet<TasksAggregator> sorted =
          waitingList.stream()
              .collect(groupingBy(AsyncClientStartupTask::order))
              .entrySet()
              .stream()
              .map(taskEntry -> new TasksAggregator(taskEntry.getKey(), taskEntry.getValue()))
              .collect(Collectors.toCollection(TreeSet::new));

      Iterator<TasksAggregator> iterator = sorted.iterator();
      TasksAggregator current = iterator.next();
      while (nonNull(current) && iterator.hasNext()) {
        TasksAggregator next = iterator.next();
        current.setNextAggregator(next);
        current = next;
      }

      ContextAggregator.waitFor(sorted).onReady(this::start);
      INITIAL_TASKS_HOLDER.attribute.forEach(
          clientStartupTask -> {
            if (!(clientStartupTask instanceof AsyncClientStartupTask)
                && !(clientStartupTask instanceof BaseRoutingStartupTask)) {
              clientStartupTask.execute();
            }
          });
      sorted.first().execute();
    } else {
      INITIAL_TASKS_HOLDER.attribute.forEach(
          clientStartupTask -> {
            if (!(clientStartupTask instanceof BaseRoutingStartupTask)) {
              clientStartupTask.execute();
            }
          });
      start();
    }
  }

  private void start() {
    INITIAL_TASKS_HOLDER.attribute.forEach(
        clientStartupTask -> {
          if ((clientStartupTask instanceof BaseRoutingStartupTask)) {
            clientStartupTask.execute();
          }
        });
    fireEvent(MainDominoEvent.class, new MainDominoEvent());
    onApplicationStarted();
  }

  private void onApplicationStarted() {
    ApplicationStartHandler applicationStartHandler = dominoOptions().getApplicationStartHandler();
    if (nonNull(applicationStartHandler)) {
      applicationStartHandler.onApplicationStarted();
    }
  }

  public void fireEvent(Class<? extends DominoEvent> eventType, DominoEvent dominoEvent) {
    LISTENERS_REPOSITORY_HOLDER.attribute.fireEvent(eventType, dominoEvent);
  }

  @FunctionalInterface
  public interface HasClientRouter {
    HasEventBus eventsBus(EventsBus eventsBus);
  }

  @FunctionalInterface
  public interface HasEventBus {
    HasDominoEventListenersRepository eventsListenersRepository(
        DominoEventsListenersRepository dominoEventsListenersRepository);
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
    HasSlotManager dominoOptions(DominoOptions dominoOptions);
  }

  @FunctionalInterface
  public interface HasSlotManager {
    CanBuildClientApp slotsManager(SlotsManager slotsManager);
  }

  @FunctionalInterface
  public interface CanBuildClientApp {
    ClientApp build();
  }

  public static class ClientAppBuilder
      implements HasClientRouter,
          HasEventBus,
          HasDominoEventListenersRepository,
          HasHistory,
          HasOptions,
          HasSlotManager,
          CanBuildClientApp {

    private RequestRouter<PresenterCommand> clientRouter;
    private EventsBus eventsBus;
    private DominoEventsListenersRepository dominoEventsListenersRepository;
    private AppHistory history;
    private AsyncRunner asyncRunner;
    private DominoOptions dominoOptions;
    private SlotsManager slotsManager;

    private ClientAppBuilder(RequestRouter<PresenterCommand> clientRouter) {
      this.clientRouter = clientRouter;
    }

    public static HasClientRouter clientRouter(RequestRouter<PresenterCommand> clientRouter) {
      return new ClientAppBuilder(clientRouter);
    }

    @Override
    public HasEventBus eventsBus(EventsBus eventsBus) {
      this.eventsBus = eventsBus;
      return this;
    }

    @Override
    public HasDominoEventListenersRepository eventsListenersRepository(
        DominoEventsListenersRepository dominoEventsListenersRepository) {
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
    public HasSlotManager dominoOptions(DominoOptions dominoOptions) {
      this.dominoOptions = dominoOptions;
      return this;
    }

    @Override
    public CanBuildClientApp slotsManager(SlotsManager slotsManager) {
      this.slotsManager = slotsManager;
      return this;
    }

    @Override
    public ClientApp build() {
      initClientApp();
      return new ClientApp();
    }

    private void initClientApp() {
      ClientApp.CLIENT_ROUTER_HOLDER.hold(clientRouter);
      ClientApp.EVENTS_BUS_HOLDER.hold(eventsBus);
      ClientApp.LISTENERS_REPOSITORY_HOLDER.hold(dominoEventsListenersRepository);
      ClientApp.HISTORY_HOLDER.hold(history);
      ClientApp.INITIAL_TASKS_HOLDER.hold(new LinkedList<>());
      ClientApp.ASYNC_RUNNER_HOLDER.hold(asyncRunner);
      ClientApp.DOMINO_OPTIONS_HOLDER.hold(dominoOptions);
      ClientApp.SLOT_MANAGER_HOLDER.hold(slotsManager);
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
