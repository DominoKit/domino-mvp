package com.progressoft.brix.domino.api.client;

@FunctionalInterface
public interface ConfigurationProvider {
    ModuleConfiguration get();
}
