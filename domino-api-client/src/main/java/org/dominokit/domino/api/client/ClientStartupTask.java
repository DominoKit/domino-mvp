package org.dominokit.domino.api.client;

@FunctionalInterface
public interface ClientStartupTask {
    void execute();
}
