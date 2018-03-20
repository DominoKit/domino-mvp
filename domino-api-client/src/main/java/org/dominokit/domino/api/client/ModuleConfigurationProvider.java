package org.dominokit.domino.api.client;

@FunctionalInterface
public interface ModuleConfigurationProvider {
    ModuleConfiguration get();
}
