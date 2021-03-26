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
package org.dominokit.domino.api.server.entrypoint;

import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;
import java.util.List;

public interface DominoHttpServerOptions {

  int getPort();

  void setPort(int port);

  String getHost();

  JsonObject toJson();

  boolean isCompressionSupported();

  int getCompressionLevel();

  boolean isAcceptUnmaskedFrames();

  int getMaxWebsocketFrameSize();

  int getMaxWebsocketMessageSize();

  String getWebsocketSubProtocols();

  boolean isHandle100ContinueAutomatically();

  int getMaxChunkSize();

  int getMaxInitialLineLength();

  int getMaxHeaderSize();

  Http2Settings getInitialSettings();

  List<HttpVersion> getAlpnVersions();

  int getHttp2ConnectionWindowSize();

  boolean isDecompressionSupported();

  int getAcceptBacklog();

  boolean isSsl();
}
