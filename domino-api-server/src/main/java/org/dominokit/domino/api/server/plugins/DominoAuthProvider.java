package org.dominokit.domino.api.server.plugins;

import io.vertx.reactivex.ext.auth.AuthProvider;
import org.dominokit.domino.api.server.PluginContext;

public interface DominoAuthProvider {
    AuthProvider getAuthProvider(PluginContext pluginContext);
}
