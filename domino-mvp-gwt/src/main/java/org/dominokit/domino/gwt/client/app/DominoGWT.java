/*
 * Copyright © 2019 Dominokit
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
package org.dominokit.domino.gwt.client.app;

// import com.google.gwt.core.client.GWT;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.InitOptions;
import org.dominokit.domino.api.client.mvp.slots.SlotsManager;
import org.dominokit.domino.client.commons.request.ClientRouter;
import org.dominokit.domino.client.history.StateHistory;
import org.dominokit.domino.gwt.client.async.GwtAsyncRunner;
import org.dominokit.domino.gwt.client.events.ClientEventFactory;
import org.dominokit.domino.gwt.client.events.ClientRequestGwtEvent;
import org.dominokit.domino.gwt.client.extensions.CustomEventsDominoEventsRepository;
import org.dominokit.domino.gwt.client.extensions.WebNamedPresenters;
import org.dominokit.domino.gwt.client.options.DefaultDominoOptions;
import org.dominokit.domino.gwt.client.slots.DefaultSlotManager;
import org.dominokit.rest.DominoRestConfig;
import org.dominokit.rest.js.DominoSimpleEventsBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DominoGWT {

  private static final Logger LOGGER = LoggerFactory.getLogger(DominoGWT.class);

  private DominoGWT() {}

  public static void init() {
    init(new DefaultInitOptions());
  }

  public static void init(InitOptions initOptions) {

    ClientRouter clientRouter = new ClientRouter(new ClientEventFactory());

    ((DominoSimpleEventsBus) DominoSimpleEventsBus.INSTANCE)
        .addEvent(ClientRequestGwtEvent.CLIENT_REQUEST_EVENT_TYPE);

    DominoRestConfig.initDefaults();
    ClientApp.ClientAppBuilder.clientRouter(clientRouter)
        .eventsBus(DominoSimpleEventsBus.INSTANCE)
        .eventsListenersRepository(new CustomEventsDominoEventsRepository())
        .history(new DominoMvpHistory(initOptions.getRootPath()))
        .asyncRunner(new GwtAsyncRunner())
        .dominoOptions(new DefaultDominoOptions())
        .slotsManager(initOptions.getSlotsManager())
        .presentersNamesRegistry(new WebNamedPresenters())
        .build();
  }

  public static class DominoMvpHistory extends StateHistory {
    public DominoMvpHistory(String rootPath) {
      super(rootPath);
    }

    @Override
    public boolean isInformOnPopState() {
      return ClientApp.make().dominoOptions().isMainApp();
    }
  }

  public static class DefaultInitOptions implements InitOptions {
    @Override
    public String getRootPath() {
      return "";
    }

    @Override
    public SlotsManager getSlotsManager() {
      return new DefaultSlotManager();
    }
  }
}
