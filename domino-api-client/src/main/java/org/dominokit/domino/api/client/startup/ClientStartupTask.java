package org.dominokit.domino.api.client.startup;

@FunctionalInterface
public interface ClientStartupTask {
    void execute();
}
