package org.dominokit.domino.api.server.entrypoint;

import org.dominokit.domino.api.server.config.ServerConfiguration;
import org.dominokit.domino.api.server.config.ServerConfiguration;

import java.util.function.Supplier;

public interface ServerContext {
    ServerConfiguration config();

    void publishEndPoint(String path, Supplier<?> factory);

    void publishService(String path, Supplier<?> factory);
}
