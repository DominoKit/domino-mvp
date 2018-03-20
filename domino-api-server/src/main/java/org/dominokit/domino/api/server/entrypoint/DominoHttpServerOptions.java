package org.dominokit.domino.api.server.entrypoint;

import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;

import java.util.List;


public interface DominoHttpServerOptions {

    int getPort();

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
