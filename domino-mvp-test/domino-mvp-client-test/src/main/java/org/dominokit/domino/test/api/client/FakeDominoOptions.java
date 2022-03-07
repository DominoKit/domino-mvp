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
package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.DominoOptions;
import org.dominokit.domino.history.TokenFilter;
import org.dominokit.rest.DominoRestConfig;
import org.dominokit.rest.shared.request.DynamicServiceRoot;

public class FakeDominoOptions implements DominoOptions {

  private ApplicationStartHandler applicationStartHandler;
  private boolean mainApp = true;
  private TokenFilterSupplier tokenFilter = TokenFilter::endsWithPathFilter;
  private TokenFilterSupplier startUpTokenFilter = TokenFilter::startsWithPathFilter;

  @Override
  public void applyOptions() {
    // no need to apply things now
  }

  @Override
  public DominoOptions addDynamicServiceRoot(DynamicServiceRoot dynamicServiceRoot) {
    DominoRestConfig.getInstance().addDynamicServiceRoot(dynamicServiceRoot);
    return this;
  }

  @Override
  public CanSetDominoOptions setApplicationStartHandler(
      ApplicationStartHandler applicationStartHandler) {
    this.applicationStartHandler = applicationStartHandler;
    return this;
  }

  @Override
  public ApplicationStartHandler getApplicationStartHandler() {
    return applicationStartHandler;
  }

  @Override
  public CanSetDominoOptions setMainApp(boolean mainApp) {
    this.mainApp = mainApp;
    return this;
  }

  @Override
  public boolean isMainApp() {
    return mainApp;
  }

  @Override
  public TokenFilter getTokenFilter(String token) {
    return tokenFilter.get(token);
  }

  @Override
  public TokenFilter getStartupTokenFilter(String token) {
    return startUpTokenFilter.get(token);
  }

  public CanSetDominoOptions setTokenFilter(TokenFilterSupplier tokenFilter) {
    this.tokenFilter = tokenFilter;
    return this;
  }

  public CanSetDominoOptions setStartUpTokenFilter(TokenFilterSupplier startUpTokenFilter) {
    this.startUpTokenFilter = startUpTokenFilter;
    return this;
  }
}
