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

import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;
import java.util.List;
import org.dominokit.domino.api.server.entrypoint.DominoHttpServerOptions;

class ImmutableHttpServerOptions implements DominoHttpServerOptions {
  private HttpServerOptions options;
  private int port;
  private String host;

  public void init(HttpServerOptions options, int port, String host) {
    this.options = options;
    this.port = port;
    this.host = host;
  }

  @Override
  public int getPort() {
    return port;
  }

  @Override
  public void setPort(int port) {
    this.port = port;
  }

  @Override
  public String getHost() {
    return host;
  }

  @Override
  public JsonObject toJson() {
    return options.toJson();
  }

  @Override
  public boolean isCompressionSupported() {
    return options.isCompressionSupported();
  }

  @Override
  public int getCompressionLevel() {
    return options.getCompressionLevel();
  }

  @Override
  public boolean isAcceptUnmaskedFrames() {
    return options.isAcceptUnmaskedFrames();
  }

  @Override
  public int getMaxWebsocketFrameSize() {
    return options.getMaxWebsocketFrameSize();
  }

  @Override
  public int getMaxWebsocketMessageSize() {
    return options.getMaxWebsocketMessageSize();
  }

  @Override
  public String getWebsocketSubProtocols() {
    return options.getWebsocketSubProtocols();
  }

  @Override
  public boolean isHandle100ContinueAutomatically() {
    return options.isHandle100ContinueAutomatically();
  }

  @Override
  public int getMaxChunkSize() {
    return options.getMaxChunkSize();
  }

  @Override
  public int getMaxInitialLineLength() {
    return options.getMaxInitialLineLength();
  }

  @Override
  public int getMaxHeaderSize() {
    return options.getMaxHeaderSize();
  }

  @Override
  public Http2Settings getInitialSettings() {
    return options.getInitialSettings();
  }

  @Override
  public List<HttpVersion> getAlpnVersions() {
    return options.getAlpnVersions();
  }

  @Override
  public int getHttp2ConnectionWindowSize() {
    return options.getHttp2ConnectionWindowSize();
  }

  @Override
  public boolean isDecompressionSupported() {
    return options.isDecompressionSupported();
  }

  @Override
  public int getAcceptBacklog() {
    return options.getAcceptBacklog();
  }

  @Override
  public boolean isSsl() {
    return options.isSsl();
  }
}
