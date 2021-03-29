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
package org.dominokit.domino.remote.logging;

import static java.util.Objects.nonNull;

import com.google.gwt.core.server.StackTraceDeobfuscator;
import java.io.*;

public class DominoStackTraceDeobfuscator extends StackTraceDeobfuscator {

  private static final String SYMBOL_MAPS_DIRECTORY = "/app/gwt/extra/app/symbolMaps/";

  @Override
  protected InputStream getSourceMapInputStream(String permutation, int fragmentNumber)
      throws IOException {
    return getInputStream(
        SYMBOL_MAPS_DIRECTORY + permutation + "_sourceMap" + fragmentNumber + ".json");
  }

  @Override
  protected InputStream getSymbolMapInputStream(String permutation) throws IOException {
    return getInputStream(SYMBOL_MAPS_DIRECTORY + permutation + ".symbolMap");
  }

  @Override
  protected InputStream openInputStream(String fileName) throws IOException {
    return new FileInputStream(new File(SYMBOL_MAPS_DIRECTORY, fileName));
  }

  private InputStream getInputStream(String path) throws FileNotFoundException {
    InputStream stream = getClass().getResourceAsStream(path);
    if (nonNull(stream)) return stream;
    throw new FileNotFoundException(path);
  }
}
