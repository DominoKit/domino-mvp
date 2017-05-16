package com.progressoft.brix.domino.api.client.history;

@FunctionalInterface
public interface PathToRequestMapperRegistry {
    void registerMapper(String path, RequestFromPath mapper);
}
