package org.dominokit.domino.api.client.request;

@FunctionalInterface
public interface CommandRegistry {
    void registerCommand(String commandName, String presenterName);
}
