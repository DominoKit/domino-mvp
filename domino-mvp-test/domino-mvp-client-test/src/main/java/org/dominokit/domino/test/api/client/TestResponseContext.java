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

import java.util.List;
import org.dominokit.domino.api.server.request.DefaultMultiMap;
import org.dominokit.domino.api.server.request.MultiMap;
import org.dominokit.domino.api.server.response.ResponseContext;
import org.dominokit.domino.api.server.response.ResponseEndHandler;
import org.dominokit.rest.shared.request.ResponseBean;

public class TestResponseContext<S extends ResponseBean> implements ResponseContext<S> {
  private S responseBean;
  private S[] responseArray;
  private List<S> responseList;
  private String bodyString;
  private boolean ended;
  private int statusCode;
  private MultiMap<String, String> headers = new DefaultMultiMap<>();
  private ResponseEndHandler endHandler;

  @Override
  public ResponseContext<S> putHeader(String name, String value) {
    headers.add(name, value);
    return this;
  }

  @Override
  public ResponseContext<S> putHeader(String name, Iterable<String> values) {
    headers.add(name, values);
    return this;
  }

  @Override
  public ResponseContext<S> statusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  @Override
  public void end() {
    this.ended = true;
    callEndHandler();
  }

  @Override
  public void end(S body) {
    this.ended = true;
    responseBean = body;
    callEndHandler();
  }

  @Override
  public void end(String body) {
    this.ended = true;
    this.bodyString = body;
    callEndHandler();
  }

  @Override
  public void end(S[] bodyArray) {
    this.ended = true;
    this.responseArray = bodyArray;
    callEndHandler();
  }

  @Override
  public void end(List<S> bodyList) {
    this.ended = true;
    this.responseList = bodyList;
    callEndHandler();
  }

  private void callEndHandler() {
    endHandler.onResponseEnded();
  }

  @Override
  public void endHandler(ResponseEndHandler endHandler) {
    this.endHandler = endHandler;
  }

  public String getBodyString() {
    return bodyString;
  }

  public boolean isEnded() {
    return ended;
  }

  public MultiMap<String, String> getHeaders() {
    return headers;
  }

  public S getResponseBean() {
    return responseBean;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public S[] getResponseArray() {
    return responseArray;
  }

  public List<S> getResponseList() {
    return responseList;
  }
}
