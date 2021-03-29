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
package org.dominokit.domino.api.server.plugins;

import static java.util.Objects.nonNull;

public class IndexPageContext {

  public static final IndexPageContext INSTANCE = new IndexPageContext();

  private IndexPageProvider indexPageProvider = DefaultIndexPageProvider.INSTANCE;

  private IndexPageContext() {}

  public IndexPageProvider getIndexPageProvider() {
    return indexPageProvider;
  }

  public void setIndexPageProvider(IndexPageProvider indexPageProvider) {
    if (nonNull(indexPageProvider)) {
      this.indexPageProvider = indexPageProvider;
    }
  }
}
