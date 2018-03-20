package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.entrypoint.DominoHttpServerOptions;
import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;

import java.util.List;

class ImmutableHttpServerOptions implements DominoHttpServerOptions {
    private HttpServerOptions options;
    private int port;
    private String host;

    public void init(HttpServerOptions options, int port, String host){
        this.options = options;
        this.port = port;
        this.host = host;
    }

    @Override
    public int getPort() {
        return port;
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
