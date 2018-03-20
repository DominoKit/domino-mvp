package org.dominokit.domino.api.client.request;

@FunctionalInterface
public interface RequestsLoader {
    void load(CommandsRepository repository);
}
