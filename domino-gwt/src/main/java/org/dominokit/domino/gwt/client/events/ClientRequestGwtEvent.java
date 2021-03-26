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
package org.dominokit.domino.gwt.client.events;

import org.dominokit.domino.rest.shared.EventProcessor;
import org.gwtproject.event.shared.Event;

public abstract class ClientRequestGwtEvent extends Event<EventProcessor> {

  public static final Event.Type<EventProcessor> CLIENT_REQUEST_EVENT_TYPE = new Event.Type<>();

  @Override
  public Type<EventProcessor> getAssociatedType() {
    return CLIENT_REQUEST_EVENT_TYPE;
  }
}
