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
package org.dominokit.domino.test.api.client;

import java.util.Map;
import org.dominokit.domino.rest.shared.Response;
import org.dominokit.domino.rest.shared.request.FailedResponseBean;
import org.dominokit.domino.rest.shared.request.ServerRequest;

public class TestFailedResponseBean extends FailedResponseBean {

  public TestFailedResponseBean() {}

  public TestFailedResponseBean(Throwable throwable) {
    super(throwable);
  }

  public <R, S> TestFailedResponseBean(ServerRequest<R, S> request, Response response) {
    super(request, response);
  }

  public void setStatusCode(int statusCode) {
    super.setStatusCode(statusCode);
  }

  public void setStatusText(String statusText) {
    setStatusText(statusText);
  }

  public void setBody(String body) {
    super.setBody(body);
  }

  public void setHeaders(Map<String, String> headers) {
    super.setHeaders(headers);
  }

  public void setThrowable(Throwable throwable) {
    super.setThrowable(throwable);
  }
}
