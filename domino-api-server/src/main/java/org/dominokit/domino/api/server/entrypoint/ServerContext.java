package org.dominokit.domino.api.server.entrypoint;

import org.dominokit.domino.api.server.config.ServerConfiguration;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ServerContext {
    ServerConfiguration config();

    void registerCleanupTask(Consumer<CleanupTask> operation);

    void publishEndPoint(String path, Supplier<?> factory);

    void publishService(String path, Supplier<?> factory);
}
