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
package org.dominokit.domino.desktop.client;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.history.*;

public class DesktopStateHistory implements AppHistory {

  private Set<HistoryListener> listeners = new HashSet<>();
  private final String rootPath;

  public DesktopStateHistory() {
    this("");
  }

  public DesktopStateHistory(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public void fireCurrentStateHistory() {
    // not implemented for desktop
  }

  @Override
  public DirectState listen(StateListener listener) {
    return listen(TokenFilter.any(), listener, false);
  }

  @Override
  public DirectState listen(StateListener listener, boolean removeOnComplete) {
    return listen(TokenFilter.any(), listener, removeOnComplete);
  }

  @Override
  public DirectState listen(TokenFilter tokenFilter, StateListener listener) {
    return listen(tokenFilter, listener, false);
  }

  @Override
  public DirectState listen(
      TokenFilter tokenFilter, StateListener listener, boolean removeOnComplete) {
    listeners.add(new HistoryListener(listener, tokenFilter, removeOnComplete));
    return new DominoDirectState(tokenFilter, currentState(), listener);
  }

  @Override
  public void removeListener(StateListener stateListener) {
    listeners.remove(stateListener);
  }

  private State currentState() {
    return new DominoHistoryState("", "", stateData(state()));
  }

  private State state() {
    return new State() {
      @Override
      public String rootPath() {
        return ClientApp.make().getHistory().getRootPath();
      }

      @Override
      public HistoryToken token() {
        return new StateHistoryToken("");
      }

      @Override
      public Optional<String> data() {
        return Optional.of("");
      }

      @Override
      public String title() {
        return "";
      }

      @Override
      public NormalizedToken normalizedToken() {
        return new DefaultNormalizedToken(new StateHistoryToken(""));
      }

      @Override
      public void setNormalizedToken(NormalizedToken normalizedToken) {}
    };
  }

  private String stateData(State state) {
    return state.data().isPresent() ? state.data().get() : "";
  }

  @Override
  public void back() {
    // not implemented for desktop
  }

  @Override
  public void forward() {
    // not implemented for desktop
  }

  @Override
  public void pushState(String token, String title, String data) {
    // not implemented for desktop
  }

  @Override
  public void pushState(String token) {
    // not implemented for desktop
  }

  @Override
  public void replaceState(String token, String title, String data) {
    // not implemented for desktop
  }

  @Override
  public void fireState(String token, String title, String data) {}

  @Override
  public void fireState(String token) {}

  @Override
  public void pushState(String token, String title, String data, TokenParameter... parameters) {}

  @Override
  public void pushState(String token, TokenParameter... parameters) {}

  @Override
  public void fireState(String token, String title, String data, TokenParameter... parameters) {}

  @Override
  public void fireState(String token, TokenParameter... parameters) {}

  @Override
  public void fireCurrentStateHistory(String title) {}

  @Override
  public int getHistoryEntriesCount() {
    return 0;
  }

  @Override
  public void pushState(String token, String title) {}

  @Override
  public void fireState(String token, String title) {}

  @Override
  public void pushState(HistoryToken token, String title, String data) {}

  @Override
  public void pushState(HistoryToken token, String title) {}

  @Override
  public void pushState(
      HistoryToken token, String title, String data, TokenParameter... parameters) {}

  @Override
  public void pushState(HistoryToken token) {}

  @Override
  public void pushState(HistoryToken token, TokenParameter... parameters) {}

  @Override
  public void fireState(HistoryToken token, String title, String data) {}

  @Override
  public void fireState(HistoryToken token, String title) {}

  @Override
  public void fireState(
      HistoryToken token, String title, String data, TokenParameter... parameters) {}

  @Override
  public void fireState(HistoryToken token) {}

  @Override
  public void fireState(HistoryToken token, TokenParameter... parameters) {}

  @Override
  public void replaceState(HistoryToken token, String title, String data) {}

  @Override
  public HistoryToken currentToken() {
    return new StateHistoryToken("");
  }

  @Override
  public String getRootPath() {
    return this.rootPath;
  }

  private class HistoryListener {
    private final StateListener listener;

    private final TokenFilter tokenFilter;

    private final boolean removeOnComplete;

    private HistoryListener(StateListener listener, TokenFilter tokenFilter) {
      this.listener = listener;
      this.tokenFilter = tokenFilter;
      this.removeOnComplete = false;
    }

    private HistoryListener(
        StateListener listener, TokenFilter tokenFilter, boolean removeOnComplete) {
      this.listener = listener;
      this.tokenFilter = tokenFilter;
      this.removeOnComplete = removeOnComplete;
    }
  }

  private class DominoHistoryState implements State {

    private final HistoryToken token;
    private final String data;
    private final String title;

    public DominoHistoryState(String token, String title, String data) {
      this.token = new StateHistoryToken(token);
      this.data = data;
      this.title = title;
    }

    @Override
    public String rootPath() {
      return DesktopStateHistory.this.rootPath;
    }

    @Override
    public HistoryToken token() {
      return this.token;
    }

    @Override
    public Optional<String> data() {
      return Optional.of(this.data);
    }

    @Override
    public String title() {
      return this.title;
    }

    @Override
    public NormalizedToken normalizedToken() {
      return new DefaultNormalizedToken(new StateHistoryToken(token.value()));
    }

    @Override
    public void setNormalizedToken(NormalizedToken normalizedToken) {}
  }
}
