package com.progressoft.brix.domino.api.client.request;

@FunctionalInterface
public interface RequestsLoader {
    void load(CommandsRepository repository);
}
