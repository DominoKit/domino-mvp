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
package org.dominokit.domino.test.apt;

import javax.annotation.processing.Processor;

public class MultipleInputSources implements InputSource {
  private String[] inputClassesNames;

  public MultipleInputSources(String[] inputClassesNames) {
    this.inputClassesNames = inputClassesNames;
  }

  @Override
  public BaseTargetProcessor withProcessors(Processor processor, Processor... rest) {
    return new MultipleTargetProcessor(inputClassesNames, processor, rest);
  }

  @Override
  public BaseTargetProcessor withProcessor(Processor processor) {
    return new MultipleTargetProcessor(inputClassesNames, processor);
  }
}
