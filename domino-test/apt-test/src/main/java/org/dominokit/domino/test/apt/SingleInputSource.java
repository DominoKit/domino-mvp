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

public class SingleInputSource implements InputSource {

  private String inputClassName;

  public SingleInputSource(String inputClassName) {
    this.inputClassName = inputClassName;
  }

  @Override
  public BaseTargetProcessor withProcessors(Processor processor, Processor... rest) {
    return new SingleTargetProcessor(inputClassName, processor, rest);
  }

  @Override
  public SingleTargetProcessor withProcessor(Processor processor) {
    return new SingleTargetProcessor(inputClassName, processor);
  }
}
