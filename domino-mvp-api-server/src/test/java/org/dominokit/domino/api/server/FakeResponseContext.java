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
package org.dominokit.domino.api.server;

import java.util.List;
import org.dominokit.domino.api.server.response.ResponseContext;
import org.dominokit.domino.api.server.response.ResponseEndHandler;

public class FakeResponseContext implements ResponseContext<TestResponse> {

  @Override
  public ResponseContext<TestResponse> putHeader(String name, String value) {
    return null;
  }

  @Override
  public ResponseContext<TestResponse> putHeader(String name, Iterable<String> values) {
    return null;
  }

  @Override
  public ResponseContext<TestResponse> statusCode(int statusCode) {
    return null;
  }

  @Override
  public void end() {}

  @Override
  public void end(TestResponse body) {}

  @Override
  public void end(String body) {}

  @Override
  public void end(TestResponse[] bodyArray) {}

  @Override
  public void end(List<TestResponse> bodyList) {}

  @Override
  public void endHandler(ResponseEndHandler endHandler) {}
}
